package advent.days

import advent.DayPuzzle
import java.util.*
import kotlin.math.abs

class Day2 : DayPuzzle<List<List<Int>>>() {
    override fun parse(scanner: Scanner): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val nums = line.split(" ").map { it.toInt() }
            list.add(nums)
        }
        return list
    }

    override fun solve1(input: List<List<Int>>): String {
        return input.count { it.isSafe() }.toString()
    }

    override fun solve2(input: List<List<Int>>): String {
        return input.count { it.isSafe2() }.toString()
    }
}

fun List<Int>.isSafe2(): Boolean {
    if (this.isSafe()) return true
    for (i in this.indices) {
        toMutableList().apply { removeAt(i) }.let {
            if (it.isSafe()) return true
        }
    }
    return false
}

fun List<Int>.isSafe(): Boolean {
    val allIncreasingOrAllDecreasing =
        this.zipWithNext().all { it.first < it.second } || this.zipWithNext().all { it.first > it.second }
    val differencesIsAlways1Or2Or3 = this.zipWithNext().all { abs(it.second - it.first) in 1..3 }
    return allIncreasingOrAllDecreasing && differencesIsAlways1Or2Or3
}
