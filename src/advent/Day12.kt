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
            n2.addConnection(n1)
        }
        val start = nodes.getValue("start")
        val end = nodes.getValue("end")

        val graph = Graph(start, nodes)
        val paths = graph.listAllPathsTo(end)
        println(paths.joinToString("\n") { it })
        return paths.size.toString()
    }
}

private data class Graph(val start: Node, val nodes: MutableMap<String, Node>) {
    var visited = mutableMapOf<String, Int>()
    val currentPath = mutableListOf<Node>()
    val simplePaths = mutableListOf<List<Node>>()
    var twiceDone = false

    val displayablePaths: String
        get() = simplePaths.joinToString("\n") { p -> p.joinToString("-") { it.name } }

    fun listAllPathsTo(end: Node): List<String> {
        visited = nodes.values.filter { !it.isMultiple }.associate {
            it.name to when (it.type) {
                Type.End, Type.Start -> 1
                Type.Standard -> 2
            }
        }.toMutableMap()
        currentPath.clear()
        simplePaths.clear()
        dfs(start, end)
        return simplePaths.map { it.joinToString("-") { n -> n.name } }
    }

    private fun dfs(start: Node, end: Node) {
        if (visited[start.name] == 0) return
        val undo = visited.toMutableMap()
        val undoTwice = twiceDone
        // Mark current node
        when {
            start.type == Type.Start || start.type == Type.End -> visited[start.name] = 0
            !twiceDone && visited[start.name] == 1 -> {
                twiceDone = true
                visited[start.name] = 0
                visited.filter { it.key != start.name && it.key != "end" && it.key != "start" }
                    .forEach { (t, _) -> visited[t] = visited.getValue(t) - 1 }
            }
            !twiceDone && !start.isMultiple -> {
                visited[start.name] = visited.getOrPut(start.name) { 2 } - 1
            }
            !start.isMultiple -> {
                visited[start.name] = 0
            }
        }
        currentPath.add(start)
        if (start == end) {
            simplePaths.add(currentPath.toList())
            visited = undo
            twiceDone = undoTwice
//            visited.remove(start.name)
            currentPath.removeLast()
            return
        }

        // recur for all the neighbours
        for (next in start.connections) {
            dfs(nodes.getValue(next), end)
        }
        currentPath.removeLast()
        visited = undo
        twiceDone = undoTwice
//        visited.remove(start.name)
    }
}


private data class Node(
    val name: String,
    val type: Type = Type.Standard,
    val connections: MutableList<String> = mutableListOf()
) {
    constructor(nodeName: String) : this(
        nodeName, when (nodeName) {
            "start" -> Type.Start
            "end" -> Type.End
            else -> Type.Standard
        }
    )

    fun addConnection(node: Node) {
        if (!connections.contains(node.name)) {
            connections += node.name
        }
        connections.sort()
    }

    val isMultiple: Boolean
        get() = type == Type.Standard && name.first().isUpperCase()

    override fun toString(): String {
        return "$name : [${
            connections.fold("") { acc, node ->
                "$acc${if (acc.isNotBlank()) "," else ""}$node"
            }
        }]"
    }

}

private enum class Type {
    Start, End, Standard
}