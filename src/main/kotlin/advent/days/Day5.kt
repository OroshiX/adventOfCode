package advent.days

import advent.DayPuzzle
import java.util.*

class Day5 : DayPuzzle<SafetyManual>() {
    override fun parse(scanner: Scanner): SafetyManual {
        val manual = ManualBuilder()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (line.isEmpty()) {
                break
            }
            val (a, b) = line.split("|")
            manual.addOrdering(a.toInt(), b.toInt())
        }

        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (line.isEmpty()) {
                break
            }
            manual.addUpdate(line.split(",").map { it.toInt() })
        }
        return manual.toSafetyManual()
    }

    override fun solve1(input: SafetyManual): String {
        return input.solve1().toString()
    }

    override fun solve2(input: SafetyManual): String {
        TODO()
    }
}

data class SafetyManual(val orderingRules: Map<Int, Ordering>, val updates: List<List<Int>>) {
    fun solve1(): Int {
        var sum = 0
        for (i in updates.indices) {
            if (isCorrectOrder(i)) {
                val median = updates[i].size / 2
                sum += updates[i][median]
            }
        }
        return sum
    }

    private fun isCorrectOrder(index: Int): Boolean {
        val update = updates[index]

        before@ for (i in update.indices) {
            for (j in i + 1 until update.size) {
                val before = update[i]
                val after = update[j]
                val ordering = orderingRules[before] ?: continue@before
                if (!ordering.afterOk(after)) {
                    return false
                }
            }
        }
        return true
    }
}

data class Ordering(val before: Set<Int>, val after: Set<Int>) {
    fun beforeOk(other: Int): Boolean {
        return before.contains(other) || !after.contains(other)
    }

    fun afterOk(other: Int): Boolean {
        return after.contains(other) || !before.contains(other)
    }
}

private data class ManualBuilder(
    val orderings: MutableMap<Int, OrderingBuilder> = mutableMapOf(),
    val updates: MutableList<List<Int>> = mutableListOf()
) {
    fun addOrdering(before: Int, after: Int) {
        val orderingBefore =
            orderings.getOrPut(before) { OrderingBuilder(mutableSetOf(), mutableSetOf()) }
        val orderingAfter =
            orderings.getOrPut(after) { OrderingBuilder(mutableSetOf(), mutableSetOf()) }
        orderingBefore.after.add(after)
        orderingAfter.before.add(before)
    }

    fun addUpdate(update: List<Int>) {
        updates.add(update)
    }

    fun toSafetyManual(): SafetyManual {
        return SafetyManual(
            orderingRules = orderings.mapValues { (_, v) ->
                Ordering(
                    before = v.before,
                    after = v.after
                )
            },
            updates = updates
        )
    }
}

private data class OrderingBuilder(val before: MutableSet<Int>, val after: MutableSet<Int>)
