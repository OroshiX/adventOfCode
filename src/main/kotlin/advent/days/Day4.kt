package advent.days

import advent.DayPuzzle
import advent.Position
import java.util.*

class Day4 : DayPuzzle<List<String>>() {
    override fun parse(scanner: Scanner): List<String> {
        val list = mutableListOf<String>()
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine())
        }
        return list
    }

    override fun solve1(input: List<String>): String {
        var sum = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                for (direction in Direction.entries) {
                    if (input.isStart(i, j, direction)) {
                        sum++
                    }
                }
            }
        }
        return sum.toString()
    }

    override fun solve2(input: List<String>): String {
        var sum = 0
        for (i in 1 until input.size - 1) {
            for (j in 1 until input[i].length - 1) {
                if (input.isXMas(i, j)) {
                    sum++
                }
            }
        }
        return sum.toString()
    }
}

fun List<String>.isXMas(i: Int, j: Int): Boolean {
    if (this[i][j] != 'A') return false
    val diag1Start = Position(i - 1, j - 1)
    val diag1End = Position(i + 1, j + 1)
    val diag2Start = Position(i - 1, j + 1)
    val diag2End = Position(i + 1, j - 1)

    val nbLines = this.size
    val nbCols = this[0].length
    if (!diag1Start.isInBounds(nbLines, nbCols) || !diag1End.isInBounds(nbLines, nbCols) ||
        !diag2Start.isInBounds(nbLines, nbCols) || !diag2End.isInBounds(nbLines, nbCols)
    ) return false

    return (this[diag1Start.i][diag1Start.j] == 'M' && this[diag1End.i][diag1End.j] == 'S' ||
            this[diag1Start.i][diag1Start.j] == 'S' && this[diag1End.i][diag1End.j] == 'M') &&
            (this[diag2Start.i][diag2Start.j] == 'M' && this[diag2End.i][diag2End.j] == 'S' ||
                    this[diag2Start.i][diag2Start.j] == 'S' && this[diag2End.i][diag2End.j] == 'M')
}

fun List<String>.isStart(i: Int, j: Int, direction: Direction): Boolean {
    val nbLines = this.size
    val nbCols = this[0].length
    for (k in word.indices) {
        val pos = Position(i + k * direction.di, j + k * direction.dj)
        if (!pos.isInBounds(nbLines, nbCols) || this[pos.i][pos.j] != word[k]) return false
    }
    return true
}

private fun Position.isInBounds(nbLines: Int, nbCols: Int): Boolean {
    return i in 0 until nbLines && j in 0 until nbCols
}

private const val word: String = "XMAS"

enum class Direction(val di: Int, val dj: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1)
}
