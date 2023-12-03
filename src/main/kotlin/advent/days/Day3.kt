package advent.days

import advent.DayPuzzle
import java.util.*

class Day3 : DayPuzzle<Grid>() {
    override fun parse(scanner: Scanner): Grid {
        val grid = mutableListOf<String>()
        val numbers = mutableMapOf<Int, List<Number>>()
        val symbols = mutableMapOf<Int, List<Symbol>>()
        var i = 0
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            numbers[i] = numbers(line)
            symbols[i] = symbols(line)
            grid += line
            i++
        }
        return Grid(grid, numbers, symbols)
    }

    private val isSymbol = "[^0-9.]".toRegex()
    private val numbersRegex = "([0-9]+)".toRegex()
    private fun numbers(line: String): List<Number> {
        val allNumbers = mutableListOf<Number>()
        numbersRegex.findAll(line).forEach {
            allNumbers += Number(it.value.toInt(), it.range.first)
        }
        return allNumbers
    }

    private fun symbols(line: String): List<Symbol> {
        val allSymbols = mutableListOf<Symbol>()
        isSymbol.findAll(line).forEach {
            allSymbols += Symbol(it.value.first(), it.range.first)
        }
        return allSymbols
    }

    override fun solve1(input: Grid): String {
        var sumPartNumbers = 0
        val linesNumbers = input.numbers
        for (line in linesNumbers) {
            for (number in line.value) {
                if (input.hasAdjacentSymbol(number, line.key)) {
                    sumPartNumbers += number.value
                }
            }
        }
        return sumPartNumbers.toString()
    }

    override fun solve2(input: Grid): String {
        var sumGears = 0
        val linesSymbols = input.symbols
        for (line in linesSymbols) {
            line.value.filter { it.value == '*' }.forEach {
                val adjacentNumbers = input.adjacentNumbers(it, line.key) ?: return@forEach
                val (n1, n2) = adjacentNumbers
                sumGears += n1.value * n2.value
            }
        }
        return sumGears.toString()
    }

}

data class Number(val value: Int, val startPosition: Int) {
    private val length = value.toString().length
    val endPosition = startPosition + length
}

data class Symbol(val value: Char, val position: Int)

data class Grid(
    val grid: List<String>,
    val numbers: Map<Int, List<Number>>,
    val symbols: Map<Int, List<Symbol>>
) {
    fun hasAdjacentSymbol(number: Number, line: Int): Boolean {
        for (i in (line - 1..line + 1)) {
            if (i < 0 || i >= grid.size) {
                continue
            }
            val symbols = symbols[i] ?: continue
            for (symbol in symbols) {
                if (symbol.position in number.startPosition - 1..number.endPosition) {
                    return true
                }
            }
        }
        return false
    }

    fun adjacentNumbers(symbol: Symbol, line: Int): Pair<Number, Number>? {
        val adjacents = mutableListOf<Number>()
        for (i in (line - 1..line + 1)) {
            if (i < 0 || i >= grid.size) {
                continue
            }
            val numbers = numbers[i] ?: continue
            for (number in numbers) {
                if (symbol.position in number.startPosition - 1..number.endPosition) {
                    adjacents.add(number)
                }
            }
        }
        return if (adjacents.size == 2) Pair(adjacents[0], adjacents[1]) else null
    }
}
