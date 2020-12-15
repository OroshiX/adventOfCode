package advent

import java.util.*

fun solve15(scanner: Scanner): String {
    with(scanner) {
        val numbers = nextLine().split(",").map { it.toInt() }
        return findNumber1(numbers).toString()
    }
}

fun findNumber1(numbers: List<Int>): Int {
    val spokenNumbers = numbers.toMutableList()
    var currentSpoken = spokenNumbers.last()
    var turn = spokenNumbers.size
    while (turn < 2020) {
        turn++
        currentSpoken = spokenNumbers.differenceTwoLastIndicesOf(currentSpoken)
        spokenNumbers.add(currentSpoken)
    }
    return currentSpoken
}

fun List<Int>.differenceTwoLastIndicesOf(number: Int): Int {
    val last = size - 1
    for (i in last - 1 downTo 0) {
        if (this[i] == number) return last - i
    }
    return 0
}