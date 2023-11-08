Overview
This is a straightforward parser designed to process the output of the Linux "tree" command and generate a Dot file. The resulting Dot file represents the directory structure as a graph.

Motivation
The Linux "tree" command is a useful tool for displaying directory structures in a hierarchical format. However, the output is primarily designed for human consumption, and extracting structured data from it can be challenging. This parser simplifies the process by converting the "tree" command's output into a graph representation, making it easier to work with and analyze the directory structure programmatically.

Example tree

Resources
├───Gaviota
├───GM
└───Imgs
    ├───Wikimedia
    │   ├───Snowish
    │   └───Ultimate
    ├───IntFiles
    │   └───Everest
    └───Figs

Dot file

digraph G {
    "Imgs" -> "Figs"
    "Imgs" -> "IntFiles"
    "Imgs" -> "Wikimedia"
    "IntFiles" -> "Everest"
    "Resources" -> "GM"
    "Resources" -> "Gaviota"
    "Resources" -> "Imgs"
    "Wikimedia" -> "Snowish"
    "Wikimedia" -> "Ultimate"
}
