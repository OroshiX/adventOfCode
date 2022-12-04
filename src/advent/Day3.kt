package advent

import java.util.*

class Day3 : DayPuzzle<List<String>>(3, false) {
    override fun parse(scanner: Scanner): List<String> {
        with(scanner) {
            val input = mutableListOf<String>()
            while (hasNextLine()) {
                input += nextLine()
            }
            return input
        }
    }


    override fun solve1(input: List<String>): String {
        return input.sumOf { rucksack ->
            rucksack.commonItem()?.priority() ?: 0
        }.toString()
    }

    override fun solve2(input: List<String>): String {
        TODO("Not yet implemented")
    }
}

private fun String.commonItem(): Char? {
    return substring(0 until length / 2).toSet()
        .intersect(substring(length / 2 until length).toSet())
        .firstOrNull()
}

private fun Char.priority(): Int {
    return if (this.isLowerCase()) {
        this - 'a' + 1
    } else {
        this - 'A' + 27
    }
}
