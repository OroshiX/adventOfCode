package advent.days

import advent.DayPuzzle
import java.util.*

class Day8 : DayPuzzle<Graph>() {
    override fun parse(scanner: Scanner): Graph {
        val path = scanner.nextLine().map { if (it == 'L') Instruction.LEFT else Instruction.RIGHT }
        scanner.nextLine()
        val map = mutableMapOf<String, Pair<String, String>>()
        val regex = """(\w+)\s+=\s+\((\w+),\s+(\w+)\)""".toRegex()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            // gets all groups of regex
            val (node, left, right) = regex.find(line)!!.destructured
            map[node] = Pair(left, right)
        }
        return Graph(path, map)
    }

    override fun solve1(input: Graph): String {
        var nbSteps = 0
        var node = "AAA"
        val instructions = input.path
        var indexInstructions = 0
        while (node != "ZZZ") {
            val currentInstruction = instructions[indexInstructions]
            node = when (currentInstruction) {
                Instruction.LEFT -> input.nodes.getValue(node).first
                Instruction.RIGHT -> input.nodes.getValue(node).second
            }
            indexInstructions = (indexInstructions + 1) % instructions.size
            nbSteps++
        }

        return nbSteps.toString()
    }

    override fun solve2(input: Graph): String {
        val optimized = optimizeInit(input)
        var nbSteps = 0
        val nodes = optimized.startNodes.map { it to false }.toMutableList()

        val instructions = if (optimized.isStartLeft) optimized.path else (listOf(0) + optimized.path)
        var indexInstructions = 0
        var l: Int
        do {
            // left loop
            l = instructions[indexInstructions]
            repeat(l) {
                nbSteps++
                for (i in nodes.indices) {
                    nodes[i] = optimized.nodes.getValue(nodes[i].first).first
                }
                if (nodes.all { it.second }) return nbSteps.toString()
            }
            indexInstructions = (indexInstructions + 1) % instructions.size

            // right loop
            l = instructions[indexInstructions]
            repeat(l) {
                nbSteps++
                for (i in nodes.indices) {
                    nodes[i] = optimized.nodes.getValue(nodes[i].first).second
                }
                if (nodes.all { it.second }) return nbSteps.toString()
            }
            indexInstructions = (indexInstructions + 1) % instructions.size
        } while (true)

        return nbSteps.toString()
    }

}

data class Graph(val path: List<Instruction>, val nodes: Map<String, Pair<String, String>>)
enum class Instruction {
    LEFT, RIGHT
}

data class OptimizedGraph(
    val path: List<Int>,
    val isStartLeft: Boolean,
    val nodes: Map<Short, Pair<Pair<Short, Boolean>, Pair<Short, Boolean>>>,
    val startNodes: Set<Short>,
)

private fun optimizeInit(input: Graph): OptimizedGraph {
    // Optimized path is the number of times we go left or right in a row
    val optimizedPath = mutableListOf<Int>()
    val startDirection = input.path.first()
    var currentInstruction = startDirection
    var currentCount = 1
    for (i in 1 until input.path.size) {
        val instruction = input.path[i]
        if (instruction == currentInstruction) {
            currentCount++
        } else {
            optimizedPath += currentCount
            currentCount = 1
            currentInstruction = instruction
        }
    }
    optimizedPath += currentCount

    val allNodes = input.nodes.keys.mapIndexed { index, key -> key to index.toShort() }.toMap()
    val optimizedNodes = mutableMapOf<Short, Pair<Pair<Short, Boolean>, Pair<Short, Boolean>>>()
    for (node in input.nodes) {
        val (left, right) = node.value
        optimizedNodes[allNodes.getValue(node.key)] =
            Pair(allNodes.getValue(left) to left.endsWith('Z'), allNodes.getValue(right) to right.endsWith('Z'))
    }
    val startNodes = input.nodes.keys.filter { it.endsWith("A") }.map { allNodes.getValue(it) }
    return OptimizedGraph(
        optimizedPath, startDirection == Instruction.LEFT, optimizedNodes, startNodes.toSet(),
    )
}