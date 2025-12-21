package advent.days

import advent.DayPuzzle
import advent.service.Progress
import java.util.*
import kotlin.math.pow

class Day8 : DayPuzzle<List<JunctionBox>>() {
    override fun parse(scanner: Scanner): List<JunctionBox> {
        val boxes = mutableListOf<JunctionBox>()
        var id = 0
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val (x, y, z) = line.split(",").map { it.toInt() }
            boxes.add(JunctionBox(x, y, z, id))
            id++
        }
        return boxes
    }

    override fun solve1(input: List<JunctionBox>, onProgressUpdate: suspend (Progress) -> Unit): String {
        val toCircuits = input.withIndex().associate { it.value.id to it.index }.toMutableMap()
        val circuits = input.associate { it.id to setOf(it.id) }.toMutableMap()
        val excludeList = mutableSetOf<Pair<JunctionBox, JunctionBox>>()
        repeat(1000) {
            connectClosest(input, toCircuits, circuits, excludeList, variation2 = false)
        }
        val sizes =
            circuits.toList().sortedBy { it.second.size }.map { it.second.size }.reversed().take(3)
        var multiplier = 1
        for (size in sizes) {
            multiplier *= size
        }
        return multiplier.toString()
    }

    override fun solve2(input: List<JunctionBox>, onProgressUpdate: suspend (Progress) -> Unit): String {
        val toCircuits = input.withIndex().associate { it.value.id to it.index }.toMutableMap()
        val circuits = input.associate { it.id to setOf(it.id) }.toMutableMap()
        val excludeList = mutableSetOf<Pair<JunctionBox, JunctionBox>>()
        var lastTwo: Pair<JunctionBox, JunctionBox>? = null
        while (circuits.size > 1) {
            lastTwo = connectClosest(input, toCircuits, circuits, excludeList, variation2 = true)
        }

        lastTwo?.let {
            return (lastTwo.first.x * lastTwo.second.x).toString()
        }
        return "There's a problem"
    }
}

data class JunctionBox(val x: Int, val y: Int, val z: Int, val id: Int) {
    fun sqrDistance(other: JunctionBox): Long {
        return ((other.x.toDouble() - x).pow(2) +
                (other.y.toDouble() - y).pow(2) +
                (other.z.toDouble() - z).pow(2)).toLong()
    }
}

private fun connectClosest(
    junctions: List<JunctionBox>,
    junctionToCircuits: MutableMap<Int, Int>,
    circuits: MutableMap<Int, Set<Int>>,
    excludeList: MutableSet<Pair<JunctionBox, JunctionBox>>,
    variation2: Boolean,
): Pair<JunctionBox, JunctionBox>? {
    val closestTwo = findClosest(junctions, excludeList, junctionToCircuits, variation2)
    if (closestTwo != null) {
        excludeList.add(closestTwo)
        val circuit1Id = junctionToCircuits.getValue(closestTwo.first.id)
        val circuit2Id = junctionToCircuits.getValue(closestTwo.second.id)
        if (circuit1Id != circuit2Id) {
            if (circuits.getValue(circuit1Id).size > circuits.getValue(circuit2Id).size) {
                // biggest is 1
                mergeCircuits(
                    eating = circuit1Id,
                    eaten = circuit2Id,
                    circuits = circuits,
                    junctionToCircuit = junctionToCircuits,
                )

            } else {
                // biggest is 2
                mergeCircuits(
                    eating = circuit2Id,
                    eaten = circuit1Id,
                    circuits = circuits,
                    junctionToCircuit = junctionToCircuits,
                )
            }

        }
    }
    return closestTwo
}

private fun mergeCircuits(
    eating: Int,
    eaten: Int,
    circuits: MutableMap<Int, Set<Int>>,
    junctionToCircuit: MutableMap<Int, Int>,
) {
    val circuitsEating = circuits.getValue(eating).toMutableSet()
    // Loop through all in eaten and add them to the
    for (oldCircuitJunction in circuits.getValue(eaten)) {
        junctionToCircuit[oldCircuitJunction] = eating
        circuitsEating.add(oldCircuitJunction)
    }
    circuits[eating] = circuitsEating

    // then remove eaten
    circuits.remove(eaten)

}

private fun findClosest(
    junctions: List<JunctionBox>,
    excludeList: Set<Pair<JunctionBox, JunctionBox>>,
    junctionToCircuits: MutableMap<Int, Int>,
    variation2: Boolean,
): Pair<JunctionBox, JunctionBox>? {
    var shortestDistance: Long? = null
    var result: Pair<JunctionBox, JunctionBox>? = null
    for ((index, junction1) in junctions.withIndex()) {
        for (k in index + 1 until junctions.size) {
            val junction2 = junctions[k]
            if (excludeList.contains(junction1, junction2) ||
                (variation2 && junctionToCircuits[junction1.id] == junctionToCircuits[junction2.id])
            ) continue
            val distance = junction1.sqrDistance(junction2)
            if (shortestDistance == null || distance < shortestDistance) {
                shortestDistance = distance
                result = junction1 to junction2
            }
        }
    }
    return result
}

private fun Set<Pair<JunctionBox, JunctionBox>>.contains(
    junction1: JunctionBox,
    junction2: JunctionBox
): Boolean {
    return this.contains(junction1 to junction2) || this.contains(junction2 to junction1)
}
