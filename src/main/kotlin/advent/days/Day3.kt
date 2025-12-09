package advent.days

import advent.DayPuzzle
import java.util.*

class Day3 : DayPuzzle<List<String>>() {
    override fun parse(scanner: Scanner): List<String> {
        val lines = mutableListOf<String>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine().trim()
            if (line.isNotEmpty()) {
                lines.add(line)
            }
        }
        return lines
    }

    override fun solve1(input: List<String>): String {
        var sum = 0
        for (line in input) {
            sum += line.biggestNumber()
        }
        return sum.toString()
    }

    override fun solve2(input: List<String>): String {
        var sum = 0L
        for (line in input) {
            sum += line.biggestNumber(12)
        }
        return sum.toString()
    }
}

private fun String.biggestNumber(): Int {
    var maxFirst = 0
    var currentFirst = 0
    var indexOfMax = -1
    for (i in 0 until this.length - 1) {
        currentFirst = this[i].digitToInt()
        if (currentFirst > maxFirst) {
            maxFirst = currentFirst
            indexOfMax = i
        }
    }
    var maxSecond = 0
    var currentSecond = 0
    for (i in indexOfMax + 1 until this.length) {
        currentSecond = this[i].digitToInt()
        if (currentSecond > maxSecond) {
            maxSecond = currentSecond
        }
    }
    return maxFirst * 10 + maxSecond
}

private fun String.biggestNumber(numberOfDigits: Int): Long {
    // The number has exactly numberOfDigits digits, so we first look for the biggest digit at maximum place length - numberOfDigits
    val maxNumber = List(numberOfDigits) { 0 }.toIntArray()
    val maxIndices = List(numberOfDigits) { -1 }.toIntArray()
    repeat(numberOfDigits) { place ->
        val startIndex = if (place == 0) 0 else maxIndices[place - 1] + 1

        for (i in startIndex until this.length - (numberOfDigits - place - 1)) {
            val currentDigit = this[i].digitToInt()
            if (currentDigit > maxNumber[place]) {
                maxNumber[place] = currentDigit
                maxIndices[place] = i
            }
        }
    }
    return maxNumber.joinToString("").toLong()
}
