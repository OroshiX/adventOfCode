package advent

import java.util.*

fun solve4(scanner: Scanner): String {
    with(scanner) {
        val tirages = nextLine().split(",").map { it.toInt() }.toMutableList()
        val boards = mutableListOf<Board>()
        nextLine() // ignore empty line
        while (hasNext()) {
            val lines = mutableListOf<String>()
            var line: String
            while (hasNext()){
                line = nextLine()
                if (line.isBlank()) break
                lines.add(line)
            }
            boards.add(Board(lines))
            lines.clear()
        }
        return game2(tirages, boards).toString()
    }
}

private fun turn(tirage: Int, boards: List<Board>): List<Int> {
    val won = mutableListOf<Int>()
    boards.forEachIndexed { index, board ->
        board.tirage(tirage)
        if (board.won()) {
            won += index
        }
    }
    return won
}

private fun game(tirages: MutableList<Int>, boards: List<Board>): Int {
    var tirage = tirages.removeFirstOrNull()

    while (tirage != null) {
        val winner = turn(tirage, boards)
        if (winner.isNotEmpty()) {
            return boards[winner.first()].score()
        }
        tirage = tirages.removeFirstOrNull()
    }
    throw IllegalStateException("Should always have a winner")
}

private fun game2(tirages: MutableList<Int>, boards: MutableList<Board>): Int {
    var tirage = tirages.removeFirstOrNull()
    while (tirage != null) {
        val winner = turn(tirage, boards)
        if (winner.isNotEmpty()) {
            var board: Board? = null
            winner.reversed().forEach {
                board = boards.removeAt(it)
            }
            if (boards.isEmpty()) {
                return board?.score() ?: 0
            }
        }
        tirage = tirages.removeFirstOrNull()
    }
    print("ok now what?")
    throw IllegalStateException("Problem in game unfolding")
}

private data class Board(val grid: Array<Array<Int?>>, var lastCalled: Int) {

    constructor(lines: List<String>) : this(lines.map {
        it.split(" ").filter { value -> value.isNotBlank() }.map { value -> value.toIntOrNull() }
            .toTypedArray()
    }.toTypedArray(), -1)

    fun tirage(number: Int) {
        lastCalled = number
        for (row in grid) {
            for (j in row.indices) {
                if (row[j] == number) {
                    row[j] = null
                }
            }
        }
    }

    fun won(): Boolean {
        // horizontal
        val wonHorizontal = grid.any { row -> row.all { it == null } }
        if (wonHorizontal) return true
        // vertical
        for (j in grid.first().indices) {
            val won = grid.all { it[j] == null }
            if (won) return true
        }
        return false
    }

    fun score(): Int {
        return grid.sumOf { row -> row.sumOf { it ?: 0 } } * lastCalled
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (!grid.contentDeepEquals(other.grid)) return false

        return true
    }

    override fun hashCode(): Int {
        return grid.contentDeepHashCode()
    }

    override fun toString(): String {
        return grid.fold("") { rowString, row ->
            rowString + "\n" + row.fold("") { acc, c ->
                "$acc ${
                    c?.toString()?.padStart(2, '0') ?: "--"
                }"
            }
        }
    }

}