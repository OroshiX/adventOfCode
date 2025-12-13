package advent.days

import advent.DayPuzzle
import java.util.*

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
        TODO()
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
