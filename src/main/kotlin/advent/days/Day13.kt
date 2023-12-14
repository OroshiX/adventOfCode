package advent.days

import advent.DayPuzzle
import java.util.*

class Day13 : DayPuzzle<List<Pattern>>() {
    override fun parse(scanner: Scanner): List<Pattern> {
        val patterns = mutableListOf<Pattern>()
        while (scanner.hasNextLine()) {
            val pattern = mutableListOf<Long>()
            var line: String
            var row = 0
            var width = 0
            do {
                line = scanner.nextLine()
                if (line.isBlank()) break
                width = line.length
                pattern += line.map { if (it == '#') '1' else '0' }.joinToString("") { it.toString() }.toLong(2)
                row++
            } while (scanner.hasNextLine())
            patterns += Pattern(pattern, width, row)
        }
        return patterns
    }

    override fun solve1(input: List<Pattern>): String {
        return input.sumOf { it.findReflection() }.toString()
    }

    private fun List<Long>.display(width: Int): String {
        return this.joinToString("\n") { it.toString(2).padStart(width, '0') }
    }

    override fun solve2(input: List<Pattern>): String {
        TODO("Not yet implemented")
    }

}

data class Pattern(val pattern: List<Long>, val width: Int, val height: Int) {
    private fun List<Long>.display(horizontal: Boolean): String {
        return joinToString("\n") { it.toString(2).padStart(if (horizontal) width else height, '0') }.replace('0', '.')
            .replace('1', '#')
    }


    fun findReflection(): Int {
        val horizontal = reflection(pattern, true)
        if (horizontal != -1) return 100 * (horizontal + 1)
        return reflection(flipPattern(pattern), false) + 1
    }

    private fun reflection(pattern: List<Long>, horizontal: Boolean): Int {
        println("Pattern:\n${pattern.display(horizontal)}\n")
        for (i in 0 until pattern.size - 1) {
            if (isReflexion(pattern, i)) return i
        }
        return -1
    }

    private fun isReflexion(pattern: List<Long>, index: Int): Boolean {
        var left = index
        var right = index + 1
        while (left >= 0 && right < pattern.size) {
            if (pattern[left] != pattern[right]) return false
            left--
            right++
        }
        return true
    }

    private fun flipPattern(pattern: List<Long>): List<Long> {
        val height = pattern.size
        val rows = pattern.map { it.toString(2).padStart(width, '0') }
        val columns = List(width) { j ->
            List(height) { i ->
                rows[i][j]
            }
        }
        return columns.map { col -> col.joinToString("") { it.toString() }.toLong(2) }
    }
}