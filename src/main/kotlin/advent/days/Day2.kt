package advent.days

import advent.DayPuzzle
import java.util.*

class Day2 : DayPuzzle<List<Range>>() {
    override fun parse(scanner: Scanner): List<Range> {
        val ranges = mutableListOf<Range>()
        val line = scanner.nextLine().trim()

        val parts = line.split(",")
        for (part in parts) {
            val bounds = part.split("-")
            val start = bounds[0].toLong()
            val end = bounds[1].toLong()
            ranges.add(Range(start, end))
        }
        return ranges
    }

    override fun solve1(input: List<Range>): String {
        var sum = 0L
        for (range in input) {
            for (id in range.iterator()) {
                if (!isValidId1(id)) {
                    sum += id
                }
            }
        }
        return sum.toString()
    }

    override fun solve2(input: List<Range>): String {
        TODO()
    }
}

data class Range(val start: Long, val end: Long) {
    fun iterator(): LongProgression {
        return start..end
    }
}

private fun isValidId1(id: Long): Boolean {
    val idString = id.toString()
    val middleIndex = idString.length / 2
    if (idString.length % 2 != 0) {
        return true
    }

    for (i in 0 until middleIndex) {
        if (idString[i] != idString[middleIndex + i]) {
            return true
        }
    }
    return false
}
