package advent.days

import advent.DayPuzzle
import java.util.*

class Day9 : DayPuzzle<List<List<Int>>>() {
    override fun parse(scanner: Scanner): List<List<Int>> {
        val input = mutableListOf<List<Int>>()
        while (scanner.hasNextLine()) {
            input += scanner.nextLine().split(" ").map { it.toInt() }
        }
        return input
    }

    override fun solve1(input: List<List<Int>>): String {
        return input.sumOf { it.treatNext(1) }.toString()
    }

    override fun solve2(input: List<List<Int>>): String {
        return input.sumOf { it.treatNext(2) }.toString()
    }
}

private fun List<Int>.treatNext(part: Int): Int {
    val rows = mutableListOf(this)
    var row: List<Int> = this
    while (row.any { it != 0 }) {
        row = row.nextRow()
        rows += row
    }
    return rows.backToTop(part == 1)
}

private fun List<List<Int>>.backToTop(last: Boolean = true): Int {
    var difference = 0
    for (k in size - 2 downTo 0) {
        val row = this[k]
        if (last) difference += row.last()
        else difference = row.first() - difference
    }
    return difference
}

private fun List<Int>.nextRow(): List<Int> {
    return List(size - 1) {
        this[it + 1] - this[it]
    }
}
