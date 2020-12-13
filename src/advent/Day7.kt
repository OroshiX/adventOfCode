package advent

import java.util.*

fun solve7(scanner: Scanner): String {
    with(scanner) {
        val map = mutableMapOf<String, Node>()
        val tree: Node
        while (hasNext()) {
            val line = nextLine()
            val ruleBag = parseToRule(line)
            val parent = map.getOrPut(ruleBag.name, { Node(ruleBag.name) })
            for (child in ruleBag.children) {
                val childNode =
                    map.getOrPut(child.second, { Node(child.second) })
                childNode.parent.add(parent)
                parent.children.add(childNode)
            }
        }
        return numberOfParents(map["shiny gold"]!!).toString()
    }
}

fun numberOfParents(node: Node): Int {
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

fun parseToRule(line: String): RuleBag {
    val matches =
        Regex("(?<parent>[\\w ]+) bags contain ((?<num1>[0-9]+) (?<child1>[\\w ]+) bags?(\\.|, (?<num2>[0-9]+) (?<child2>[\\w ]+) bags?\\.)|(?<noChild>no other bags.))").matchEntire(
            line
        ) ?: throw IllegalArgumentException()

    val parent = matches.groups["parent"]?.value
        ?: throw IllegalArgumentException("No parent")
    val noChild = matches.groups["noChild"]?.value
    if (noChild != null) return RuleBag(parent, listOf())
    val num1 = matches.groups["num1"]?.value?.toInt()
        ?: throw IllegalArgumentException("Num1 should not be null")
    val child1 = matches.groups["child1"]?.value
        ?: throw IllegalArgumentException("Child 1 should not be null")
    val num2 = matches.groups["num2"]?.value?.toInt()
    val child2 = matches.groups["child2"]?.value

    if (num2 != null && child2 != null) {
        return RuleBag(parent, listOf(Pair(num1, child1), Pair(num2, child2)))
    }
    return RuleBag(parent, listOf(Pair(num1, child1)))
}

data class RuleBag(val name: String, val children: List<Pair<Int, String>>)
data class Node(
    val name: String,
    val children: MutableList<Node> = mutableListOf(),
    val parent: MutableList<Node> = mutableListOf()
)