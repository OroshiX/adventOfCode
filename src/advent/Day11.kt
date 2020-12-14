package advent

import java.lang.Integer.max
import java.util.*
import kotlin.math.min

fun solve11(scanner: Scanner): String {
    with(scanner) {
        val readGrid = mutableListOf<Array<Seat>>()
        while (hasNext()) {
            val line = nextLine()
            val row = line.map {
                when (it) {
                    'L' -> Seat.EMPTY
                    '.' -> Seat.FLOOR
                    '#' -> Seat.OCCUPIED
                    else -> throw IllegalArgumentException("Unknown sign $it")
                }
            }.toTypedArray()
            readGrid.add(row)
        }
        val nLines: Int = readGrid.size
        val nColumns: Int = readGrid.first().size
        val grid: Array<Array<Seat>> = readGrid.toTypedArray()
        var current: Array<Array<Seat>>
        do {
            current = Array(grid.size) { grid[it].clone() }
            transform(current, grid, nLines, nColumns)
        } while (!current.contentDeepEquals(grid))
        val showGrid: String = grid.toPrintableString()
        println(showGrid)
        return countOccupied(grid).toString()
    }
}

fun countOccupied(grid: Array<Array<Seat>>): Int = grid.sumBy { row ->
    row.sumBy {
        when (it) {
            Seat.OCCUPIED -> 1
            Seat.FLOOR, Seat.EMPTY -> 0
        }
    }
}

fun transform(
    grid: Array<Array<Seat>>,
    endGrid: Array<Array<Seat>>,
    nLines: Int,
    nColumns: Int
) {
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (endGrid[i][j] == Seat.FLOOR) continue
            when (countNeighbours(grid, i, j, nLines, nColumns)) {
                0 -> endGrid[i][j] = Seat.OCCUPIED
                1, 2, 3 -> {
                }
                else -> endGrid[i][j] = Seat.EMPTY
            }
        }
    }
}

fun countNeighbours(
    grid: Array<Array<Seat>>,
    i: Int,
    j: Int,
    nLines: Int,
    nColumns: Int
): Int {
    var sum = 0
    for (y in max(i - 1, 0)..min(i + 1, nLines - 1)) {
        for (x in max(j - 1, 0)..min(j + 1, nColumns - 1)) {
            if (y == i && x == j) continue
            when (grid[y][x]) {
                Seat.OCCUPIED -> sum++
                Seat.FLOOR, Seat.EMPTY -> {
                }
            }
        }
    }
    return sum
}

enum class Seat {
    OCCUPIED, FLOOR, EMPTY;

    override fun toString(): String = when (this) {
        OCCUPIED -> "#"
        FLOOR -> "."
        EMPTY -> "L"
    }
}

fun Array<Array<Seat>>.toPrintableString(): String {
    return joinToString("\n") {
        it.joinToString("")
    }
}