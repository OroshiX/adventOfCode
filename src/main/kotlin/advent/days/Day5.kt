package advent.days

import advent.DayPuzzle
import java.util.*
import kotlin.math.max
import kotlin.math.sign

class Day5 : DayPuzzle<IngredientList>() {
    override fun parse(scanner: Scanner): IngredientList {
        val ranges = mutableListOf<LongRange>()
        val ingredientList = mutableListOf<Long>()

        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (line.isBlank()) {
                break
            }
            val (start, end) = line.split("-").map { it.toLong() }
            ranges.add(start..end)
        }
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            ingredientList.add(line.toLong())
        }
        return IngredientList(ranges, ingredientList)
    }

    override fun solve1(input: IngredientList): String {
        var sum = 0
        for (ingredient in input.ingredients) {
            if (input.isFresh(ingredient)) {
                sum++
            }
        }
        return sum.toString()
    }

    override fun solve2(input: IngredientList): String {
        val sortRanges = input.ranges.sortRanges()
        val separatedList = sortRanges.toSeparateRanges()
        var sum = 0L
        for (separated in separatedList) {
            sum += separated.last - separated.first + 1
        }
        return sum.toString()
    }
}

data class IngredientList(
    val ranges: List<LongRange>,
    val ingredients: List<Long>,
) {
    fun isFresh(ingredient: Long): Boolean {
        return ranges.any { it.contains(ingredient) }
    }

}

private fun List<LongRange>.sortRanges(): List<LongRange> {
    return sortedWith { o1, o2 ->
        when {
            o1.first == o2.first -> (o2.last - o1.last).sign
            else -> (o1.first - o2.first).sign
        }
    }
}

fun areSeparated(range1: LongRange, range2: LongRange): Boolean {
    return when {
        range1.first > range2.first -> throw IllegalStateException("Badly ordered ranges ${range1.prettyString()} and ${range2.prettyString()}")
        range1.first > range2.last -> throw IllegalStateException("Badly ordered ranges ${range1.prettyString()} and ${range2.prettyString()}")
        range2.first <= range1.last -> false
        range2.first > range1.last -> true
        else -> throw IllegalStateException("Should not happen")
    }
}

private fun List<LongRange>.toSeparateRanges(): List<LongRange> {
    val ranges = mutableListOf<LongRange>()
    var i = 0
    var range1 = this[0]
    var lastAddedIndex = 0
    ranges.add(range1)
    outer@ while (i < this.size) {
        for (j in i + 1 until this.size) {
            val range2 = this[j]

            if (areSeparated(range1, range2)) {
                ranges.add(range2)
                lastAddedIndex++
                i = j
                range1 = range2
                continue@outer
            }
            // ranges are overlapping
            val newRange = mergeRanges(range1, range2)
            ranges[lastAddedIndex] = newRange
            range1 = newRange
            i = j
        }
        i++
        if (i >= size) break@outer
        range1 = this[i]
    }
    return ranges
}

private fun mergeRanges(range1: LongRange, range2: LongRange): LongRange {
    if (range1.last < range2.first || range2.last < range1.first) {
        throw IllegalStateException("Ranges are separated ${range1.prettyString()} and ${range2.prettyString()}")
    }
    return range1.first..max(range1.last, range2.last)
}

fun LongRange.prettyString(): String {
    return first.prettyPrint() + ".." + last.prettyPrint()
}

fun Long.prettyPrint(): String {
    return toString().reversed().chunked(3).joinToString("'").reversed()
}
