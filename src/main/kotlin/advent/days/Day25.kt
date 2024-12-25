package advent.days

import advent.DayPuzzle
import java.util.*

class Day25 : DayPuzzle<List<Schematics>>() {
    override fun parse(scanner: Scanner): List<Schematics> {
        val schematics = mutableListOf<Schematics>()
        var id = 0
        while (scanner.hasNextLine()) {
            val grid = mutableListOf<List<Char>>()
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                if (line.isBlank()) {
                    break
                }
                grid.add(line.toList())
            }
            // Count all # vertically in each column
            val profile = grid[0].indices.map { j -> grid.count { it[j] == '#' } - 1 }
            val height = grid.size - 2

            val schematic = when {
                grid.first().all { it == '#' } -> Schematics.Lock(id, profile, height)
                grid.last().all { it == '#' } -> Schematics.Key(id, profile, height)
                else -> throw IllegalArgumentException("Unknown schematics type")
            }
            schematics.add(schematic)
            id++
        }
        return schematics
    }

    override fun solve1(input: List<Schematics>): String {
        var countMatches = 0
        val keys = input.filterIsInstance<Schematics.Key>()
        val locks = input.filterIsInstance<Schematics.Lock>()
        for (key in keys) {
            for (lock in locks) {
                if (key.match(lock)) {
                    countMatches++
                }
            }
        }
        return countMatches.toString()
    }

    override fun solve2(input: List<Schematics>): String {
        TODO()
    }
}

sealed class Schematics(open val profile: List<Int>, open val height: Int) {
    fun match(other: Schematics): Boolean {
        if (!opposite(other)) return false
        for (j in profile.indices) {
            if (profile[j] + other.profile[j] > height) return false
        }
        return true
    }

    abstract fun opposite(other: Schematics): Boolean
    data class Lock(val id: Int, override val profile: List<Int>, override val height: Int) :
        Schematics(profile, height) {
        override fun opposite(other: Schematics): Boolean {
            return other is Key
        }
    }

    data class Key(val id: Int, override val profile: List<Int>, override val height: Int) :
        Schematics(profile, height) {
        override fun opposite(other: Schematics): Boolean {
            return other is Lock
        }
    }
}
