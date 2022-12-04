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
        var prioritySum = 0
        var group = mutableListOf<String>()
        for (i in input.indices) {
            group += input[i]
            if (i % 3 == 2) {
                val common = group.commonItem()
                prioritySum += common.priority()
                group = mutableListOf()
            }
        }
        return prioritySum.toString()
    }
}

private fun List<String>.commonItem(): Char {
    var common = first().toSet()
    for (s in this) {
        common = common.intersect(s.toSet())
    }
    return common.first()
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
