package advent

import java.util.*

class Day4 : DayPuzzle<List<Pair<IntRange, IntRange>>>(4, false) {
    override fun parse(scanner: Scanner): List<Pair<IntRange, IntRange>> {
        with(scanner) {
            val input = mutableListOf<Pair<IntRange, IntRange>>()
            while (hasNextLine()) {
                val (first, second) = nextLine().split(",").map { it.split("-") }
                input += first[0].toInt()..first[1].toInt() to second[0].toInt()..second[1].toInt()
            }
            return input
        }
    }

    override fun solve1(input: List<Pair<IntRange, IntRange>>): String {
        return input.sumOf { pair: Pair<IntRange, IntRange> ->
            val res: Int = if (pair.first.toList().containsAll(pair.second.toList()) ||
                pair.second.toList().containsAll(pair.first.toList())
            ) 1 else 0
            res
        }.toString()
    }

    override fun solve2(input: List<Pair<IntRange, IntRange>>): String {
        TODO("Not yet implemented")
    }
}
