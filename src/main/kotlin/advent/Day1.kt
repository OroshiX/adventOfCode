package advent

import java.util.*

class Day1 : DayPuzzle<List<String>>(Part.TWO, true) {
    override fun parse(scanner: Scanner): List<String> {
        val list = mutableListOf<String>()
        while (scanner.hasNextLine()) {
            list += scanner.nextLine()
        }
        return list
    }

    override fun solve1(input: List<String>): String {
        var sum = 0
        for (s in input) {
            sum += extractNumber1(s)
        }
        return sum.toString()
    }

    private fun extractNumber1(input: String): Int {
        val regexFirstDigit = "^[^0-9]*([0-9])".toRegex()
        val firstDigit = regexFirstDigit.find(input)?.groupValues?.get(1)
            ?: throw IllegalArgumentException("Input $input doesn't have digits")
        val regexLastDigit = "([0-9])[^0-9]*$".toRegex()
        val lastDigit = regexLastDigit.find(input)?.groupValues?.get(1)
            ?: throw IllegalArgumentException("Input $input doesn't have digits")
        return (firstDigit + lastDigit).toInt()
    }

    private fun extractNumber2(input: String): Long {
        val firstDigit = regexGroupDigits.find(input)?.groupValues?.get(1)
            ?: throw IllegalArgumentException("Input $input doesn't have digits")
        val lastDigit = inverseGroupDigits.find(input.reversed())?.groupValues?.get(1)
            ?: throw IllegalArgumentException("Input ${input.reversed()} doesn't have digits")
        return (mapDigits.getValue(firstDigit).toString() + mapDigits.getValue(lastDigit.reversed())
            .toString())
            .toLong()
    }

    override fun solve2(input: List<String>): String {
        var sum = 0L
        for (s in input) {
            sum += extractNumber2(s)
        }
        return sum.toString()
    }

}

private val mapDigits = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
) + (1..9).associateBy { it.toString() }
private val regexDigits by lazy {
    val iterator = mapDigits.iterator()
    buildString {
        append(iterator.next().key)
        while (iterator.hasNext()) {
            append("|")
            append(iterator.next().key)
        }
    }
}
private val regexGroupDigits by lazy {
    "($regexDigits)".toRegex()
}
private val inverseGroupDigits by lazy {
    "(${regexDigits.reversed()})".toRegex()
}
