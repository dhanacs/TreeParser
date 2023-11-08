/**
 * Represents a node in the processing stack.
 */
class Node {
    String name;
    Integer depth;

    Node(name, depth) {
        this.name = name;
        this.depth = depth;
    }
}

/**
 * Get the edge between two nodes for the DOT file.
 * @param a The source node name.
 * @param b The target node name.
 * @return The edge representation as a string.
 */
def encodeEdge(a, b) {
    if (a.equals(b)) return "";

    def quote = "\"";
    return (quote + a + quote) + " -> " + (quote + b + quote);
}

/**
 * Write the edges to the DOT file.
 * @param dotFile The FileWriter for the DOT file.
 * @param edges The list of edges to be written.
 */
def writeDotFile(dotFile, edges) {
    edges.sort();
    dotFile << "digraph G {\n"
    edges.each { edge ->
        dotFile << (edge + "\n");
    }

    dotFile << "}\n"
    dotFile.close();
}

// Check if the given character is a valid starting character
// Return true if the character is alphanumeric or one of the specified special characters.
def alphaNumeric(c) {
    boolean flag = (c >= 'a' && c <= 'z');
    flag = flag || (c >= 'A' && c <= 'Z');
    flag = flag || (c >= '0' && c <= '9');
    flag = flag || (c == '_');
    flag = flag || (c == '.');

    return flag;
}

// Extract name & depth from the line
def getNameDepth(line) {
    // Find the index of the first alphanumeric character in the line.
    int depth = line.findIndexOf { c ->
        alphaNumeric(c)
    };
    depth = depth == -1 ? 0 : depth;
    def name = line ? line.substring(depth).trim() : "";

    return [name, depth];
}

// Insert the edge if it is not seen already
def insertEdge(a, b, edges, Set<String> edgesSet) {
    def edge = encodeEdge(a, b);
    if (edge && !edgesSet.contains(edge)) {
        edges.push(edge);
        edgesSet.add(edge);
    }
}

/**
 * Parse the dependency tree and generate a list of edges for the DOT file.
 * @param lines The input lines representing the dependency tree.
 * @param edges The list to store the edge representations.
 */
def parseDependencyTree(lines, edges) {
    Set<String> edgesSet = [];
    if (lines.size() <= 1) return;

    def i = 0;
    def (name, depth) = getNameDepth(lines[i]);

    Stack<Node> S = [];
    S.push(new Node(name, depth));
    ++i;
    while (!S.empty()) {
        Node previous = S.last();

        (name, depth) = getNameDepth(lines[i]);
        if (depth > previous.depth && (previous.depth != -1)) {
            insertEdge(previous.name, name, edges, edgesSet);
        } else {
            // Pop nodes from the stack until a suitable depth is found.
            while (!S.empty() && S.last().depth >= depth) {
                S.pop();
            }

            // Insert an edge between the top node on the stack and the current node.
            if (!S.empty()) {
                def top = S.last();
                insertEdge(top.name, name, edges, edgesSet);
            }
        }

        // Push the current node onto the stack.
        def current = new Node(name, depth)
        if (i < lines.size() && name) {
            S.push(current);
            ++i;
        } else {
            if (!S.empty()) S.pop();
        }
    }
}

// Main callers
def edges = [];
def fileLines = new FileReader("Tree.txt").readLines();
def dotFile = new FileWriter("dependency.dot");
parseDependencyTree(fileLines, edges);
writeDotFile(dotFile, edges);
println("Dependency tree DOT file created successfully!");
