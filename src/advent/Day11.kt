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
            transform2(current, grid, nLines, nColumns)
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

fun transform2(
    grid: Array<Array<Seat>>,
    endGrid: Array<Array<Seat>>,
    nLines: Int,
    nColumns: Int
) {
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (endGrid[i][j] == Seat.FLOOR) continue
            when (countNeighbours2(grid, i, j, nLines, nColumns)) {
                0 -> endGrid[i][j] = Seat.OCCUPIED
                1, 2, 3, 4 -> {
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

fun countNeighbours2(
    grid: Array<Array<Seat>>,
    i0: Int,
    j0: Int,
    nLines: Int,
    nColumns: Int
): Int {
    var sum = 0
    // dir top
    t@ for (i in i0 - 1 downTo 0) {
        when {
            isFloor(i, j0, grid) -> continue@t
            isOccupied(i, j0, grid) -> sum++
        }
        break@t
    }
    // dir bottom
    b@ for (i in i0 + 1 until nLines) {
        when {
            isFloor(i, j0, grid) -> continue@b
            isOccupied(i, j0, grid) -> sum++
        }
        break@b
    }
    // dir left
    l@ for (j in j0 - 1 downTo 0) {
        when {
            isFloor(i0, j, grid) -> continue@l
            isOccupied(i0, j, grid) -> sum++
        }
        break@l
    }
    // dir right
    r@ for (j in j0 + 1 until nColumns) {
        when {
            isFloor(i0, j, grid) -> continue@r
            isOccupied(i0, j, grid) -> sum++
        }
        break@r
    }
    val size = min(nLines, nColumns)
    // dir TL
    tl@ for (k in 1 until size) {
        val i = i0 - k
        val j = j0 - k
        when {
            !isLegal(i, j, nLines, nColumns) -> break@tl
            isFloor(i, j, grid) -> continue@tl
            isOccupied(i, j, grid) -> sum++
        }
        break@tl
    }
    // dir TR
    tr@ for (k in 1 until size) {
        val i = i0 - k
        val j = j0 + k
        when {
            !isLegal(i, j, nLines, nColumns) -> break@tr
            isFloor(i, j, grid) -> continue@tr
            isOccupied(i, j, grid) -> sum++
        }
        break@tr
    }
    // dir BL
    bl@ for (k in 1 until size) {
        val i = i0 + k
        val j = j0 - k
        when {
            !isLegal(i, j, nLines, nColumns) -> break@bl
            isFloor(i, j, grid) -> continue@bl
            isOccupied(i, j, grid) -> sum++
        }
        break@bl
    }
    // dir BR
    br@ for (k in 1 until size) {
        val i = i0 + k
        val j = j0 + k
        when {
            !isLegal(i, j, nLines, nColumns) -> break@br
            isFloor(i, j, grid) -> continue@br
            isOccupied(i, j, grid) -> sum++
        }
        break@br
    }

    return sum
}

private fun isLegal(i: Int, j: Int, nLines: Int, nColumns: Int): Boolean {
    return i >= 0 && j >= 0 && i < nLines && j < nColumns
}

private fun isFloor(i: Int, j: Int, grid: Array<Array<Seat>>): Boolean {
    return grid[i][j] == Seat.FLOOR
}

private fun isOccupied(i: Int, j: Int, grid: Array<Array<Seat>>): Boolean {
    return grid[i][j] == Seat.OCCUPIED
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