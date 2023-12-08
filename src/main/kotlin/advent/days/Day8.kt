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
        var nbSteps = 0
        var nodes = input.nodes.keys.filter { it.endsWith("A") }
        val instructions = input.path
        var indexInstructions = 0
        var endCondition: Boolean
        do {
            val currentInstruction = instructions[indexInstructions]
            endCondition = true
            nodes = nodes.map {
                val next = input.nodes.getValue(it)
                when (currentInstruction) {
                    Instruction.LEFT -> {
                        if (!next.first.endsWith('Z')) endCondition = false
                        next.first
                    }
                    Instruction.RIGHT -> {
                        if (!next.second.endsWith('Z')) endCondition = false
                        next.second
                    }
                }
            }
            indexInstructions = (indexInstructions + 1) % instructions.size
            nbSteps++
        } while (!endCondition)

        return nbSteps.toString()
    }

}

data class Graph(val path: List<Instruction>, val nodes: Map<String, Pair<String, String>>)
enum class Instruction {
    LEFT, RIGHT
}