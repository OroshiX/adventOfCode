package advent.days

import advent.DayPuzzle
import advent.Position
import java.util.*

class Day7 : DayPuzzle<RayGrid>() {
    override fun parse(scanner: Scanner): RayGrid {
        val grid = mutableListOf<List<RayCell>>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            grid.add(line.map { RayCell.from(it) })
        }
        return RayGrid(rows = grid.size, cols = grid.first().size, grid = grid)
    }

    override fun solve1(input: RayGrid): String {
        return input.howManySplits().toString()
    }

    override fun solve2(input: RayGrid): String {
        TODO()
    }
}

data class RayGrid(val rows: Int, val cols: Int, val grid: List<List<RayCell>>) {
    val startPosition: Position
        get() {
            val row = grid.indexOfFirst { row -> row.contains(RayCell.START) }
            val col = grid[row].indexOfFirst { col -> col == RayCell.START }
            return Position(row, col)
        }

    fun stepRayPositions(raysColumns: Set<Int>, stepNumber: Int): Pair<Set<Int>, Int> {
        var nbSplits = 0
        val newRays = mutableSetOf<Int>()

        if (grid[stepNumber].contains(RayCell.SPLITTER).not()) {
            // No splitter
            return raysColumns to 0
        }

        for (column in raysColumns) {
            if (grid[stepNumber][column] == RayCell.SPLITTER) {
                nbSplits++
                newRays.add(column - 1)
                newRays.add(column + 1)
            } else {
                newRays.add(column)
            }
        }

        return newRays to nbSplits
    }

    fun howManySplits(): Int {
        val start = startPosition
        var currentRays = setOf(start.j)
        var currentStep = start.i
        var sum = 0
        while (currentStep < grid.size) {
            val (newRays, nbSplits) = stepRayPositions(currentRays, currentStep)
            currentRays = newRays
            sum += nbSplits
            currentStep++
        }
        return sum
    }
}

enum class RayCell(val char: Char) {
    EMPTY('.'),
    SPLITTER('^'),
    START('S');

    companion object {
        fun from(char: Char): RayCell {
            return RayCell.entries.first { char == it.char }
        }
    }
}
