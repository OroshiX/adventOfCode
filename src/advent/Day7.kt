package advent

import java.util.*

fun solve7(scanner: Scanner): String {
    with(scanner) {
        val map = mutableMapOf<String, Node>()
        while (hasNext()) {
            val line = nextLine()
            val ruleBag = parseToRule(line)
            val parent = map.getOrPut(ruleBag.name, { Node(ruleBag.name) })
            for (child in ruleBag.children) {
                val childNode =
                    map.getOrPut(child.second, { Node(child.second) })
                childNode.parent.add(parent)
                parent.children.add(Pair(childNode, child.first))
            }
        }
        return numberOfChildren2(map["shiny gold"]!!).toString()
    }
}

fun numberOfParents1(node: Node): Int {
    val queue = ArrayDeque<Node>()
    queue.add(node)
    val parents = mutableSetOf<String>()
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        queue.addAll(current.parent)
        parents.addAll(current.parent.map { it.name })
    }
    return parents.size
}

fun numberOfChildren2(node: Node): Int {
    val queue = ArrayDeque<Pair<Node, Int>>()
    queue.add(Pair(node, 1))
    var bags = 0
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        queue.addAll(current.first.children.map {
            Pair(
                it.first,
                it.second * current.second
            )
        })
        bags += current.second
    }
    return bags-1

}

fun parseToRule(line: String): RuleBag {
    val matches =
        Regex("(?<parent>[\\w ]+) bags contain ((?<noChild>no other bags)|[0-9]+ [ \\w,]+ bags?)\\.").matchEntire(
            line
        ) ?: throw IllegalArgumentException()

    val parent = matches.groups["parent"]?.value
        ?: throw IllegalArgumentException("No parent")
    val noChild = matches.groups["noChild"]?.value
    if (noChild != null) return RuleBag(parent, listOf())

    val childrenMatches =
        Regex("(?<num>[0-9]+) (?<child>[\\w ]+) bags?").findAll(line)
    val children = mutableListOf<Pair<Int, String>>()
    for (childMatch in childrenMatches) {
        val num = childMatch.groups["num"]?.value?.toInt()
            ?: throw IllegalArgumentException("Num should not be null")
        val child =
            childMatch.groups["child"]?.value ?: throw IllegalArgumentException(
                "Child should not be null"
            )
        children.add(Pair(num, child))
    }
    return RuleBag(parent, children)
}

data class RuleBag(val name: String, val children: List<Pair<Int, String>>)
data class Node(
    val name: String,
    val children: MutableList<Pair<Node, Int>> = mutableListOf(),
    val parent: MutableList<Node> = mutableListOf()
)