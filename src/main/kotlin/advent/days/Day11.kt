package advent.days

import advent.DayPuzzle
import advent.Position
import java.util.*
import kotlin.math.abs

class Day11 : DayPuzzle<Galaxy>() {
    override fun parse(scanner: Scanner): Galaxy {
        var row = 0
        val positions = mutableListOf<Position>()
        var emptyCols = mutableSetOf<Int>()
        val emptyRows = mutableSetOf<Int>()
        var isInitialized = false
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (!isInitialized) {
                emptyCols = List(line.length) { it }.toMutableSet()
                isInitialized = true
            }
            if (line.all { it == '.' }) {
                emptyRows += row
                row++
                continue
            }
            line.forEachIndexed { index, c ->
                if (c == '#') {
                    positions += Position(row, index)
                    emptyCols.remove(index)
                }
            }
            row++
        }
        return Galaxy(positions, emptyRows, emptyCols)
    }

    override fun solve1(input: Galaxy): String {
        println(input.display())
        return input.distances(2).toString()
    }

    override fun solve2(input: Galaxy): String {
        return input.distances(1_000_000).toString()
    }

}

data class Galaxy(val positions: List<Position>, val emptyRows: Set<Int>, val emptyCols: Set<Int>) {
    private val width: Int
        get() = positions.maxOf { it.j } + 1
    private val height: Int
        get() = positions.maxOf { it.i } + 1

    fun display(): String {
        val grid = MutableList(height) { "".padEnd(width, '.').toMutableList() }
        positions.forEach { grid[it.i][it.j] = '#' }
        return grid.joinToString("\n") { it.joinToString("") }
    }

    private fun expand(amount: Int): List<Position> {
        val newPositions = positions.sortedBy { it.j }.toMutableList()
        for (j in emptyCols.sortedDescending()) {
            val index = newPositions.indexOfFirst { it.j > j }
            if (index == -1) continue
            for (k in index until newPositions.size) {
                newPositions[k] = newPositions[k].copy(j = newPositions[k].j + amount - 1)
            }
        }
        newPositions.sortBy { it.i }
        for (i in emptyRows.sortedDescending()) {
            val index = newPositions.indexOfFirst { it.i > i }
            if (index == -1) continue
            for (k in index until newPositions.size) {
                newPositions[k] = newPositions[k].copy(i = newPositions[k].i + amount - 1)
            }
        }
        return newPositions
    }

    fun distances(amount: Int): Long {
        val positions = expand(amount)
        var distance = 0L
        for (i1 in positions.indices) {
            for (i2 in i1 + 1 until positions.size) {
                val p1 = positions[i1]
                val p2 = positions[i2]
                distance += p1.distance(p2)
            }
        }
        return distance
    }
}

private fun Position.distance(other: Position): Int {
    return abs(this.i - other.i) + abs(this.j - other.j)
}
