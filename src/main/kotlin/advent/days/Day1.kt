package advent.days

import advent.DayPuzzle
import java.util.*
import kotlin.math.abs

class Day1 : DayPuzzle<Pair<List<Int>, List<Int>>>() {

    override fun parse(scanner: Scanner): Pair<List<Int>, List<Int>> {
        val list1 = mutableListOf<Int>()
        val list2 = mutableListOf<Int>()
        while (scanner.hasNextInt()) {
            list1.add(scanner.nextInt())
            list2.add(scanner.nextInt())
        }
        return list1 to list2
    }

    override fun solve1(input: Pair<List<Int>, List<Int>>): String {
        val sorted = input.first.sorted()
        val sorted2 = input.second.sorted()
        val sumDiffs = (sorted zip sorted2).sumOf { abs(it.second - it.first) }
        return sumDiffs.toString()
    }

    override fun solve2(input: Pair<List<Int>, List<Int>>): String {
        val similarity = mutableMapOf<Int, Int>()
        var sum = 0
        for (i in input.first) {
            sum += similarity.getOrPut(i) { input.second.count { it == i } } * i
        }
        return sum.toString()
    }
}
