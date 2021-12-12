package advent

import java.util.*

fun solve12(scanner: Scanner): String {
    with(scanner) {
        val nodes = mutableMapOf<String, Node>()
        while (hasNext()) {
            val (node1, node2) = nextLine().split("-")
            val n1 = nodes.getOrPut(node1) { Node(node1) }
            val n2 = nodes.getOrPut(node2) { Node(node2) }
            n1.addConnection(n2)
        }
        val start = nodes.getValue("start")
        val end = nodes.getValue("end")
        val paths = listAllPaths(start, end)
        println(paths)
        return paths.size.toString()
    }
}

private fun listAllPaths(start: Node, end: Node): List<String> {
    val visited = mutableSetOf<String>()
    val currentPath = mutableListOf<Node>()
    val simplePaths = mutableListOf<MutableList<Node>>()

    dfs(start, end, visited, currentPath, simplePaths)
    return simplePaths.map { it.joinToString(",") { n -> n.name } }
}

private fun dfs(
    start: Node,
    end: Node,
    visited: MutableSet<String>,
    currentPath: MutableList<Node>,
    simplePaths: MutableList<MutableList<Node>>
) {
    if (visited.contains(start.name)) return

    // Mark current node
    if (!start.isMultiple) {
        visited.add(start.name)
    }
    currentPath.add(start)
    if (start == end) {
        simplePaths.add(currentPath)
        visited.remove(start.name)
        currentPath.removeLast()
        return
    }

    // recur for all the neighbours
    for (next in start.connections) {
        dfs(next, end, visited, currentPath, simplePaths)

    }
    currentPath.removeLast()
    visited.remove(start.name)
}


private data class Node(
    val name: String,
    val type: Type = Type.Standard,
    val connections: MutableList<Node> = mutableListOf()
) {
    constructor(nodeName: String) : this(
        nodeName, when (nodeName) {
            "start" -> Type.Start
            "end" -> Type.End
            else -> Type.Standard
        }
    )

    fun addConnection(node: Node) {
        if (!connections.contains(node)) {
            connections += node
            node.addConnection(this)
        }
    }

    val isMultiple: Boolean
        get() = type == Type.Standard && name.first().isUpperCase()

    override fun toString(): String {
        return "$name : [${
            connections.fold("") { acc, node ->
                "$acc${if (acc.isNotBlank()) "," else ""}${node.name}"
            }
        }]"
    }

}

private enum class Type {
    Start, End, Standard
}