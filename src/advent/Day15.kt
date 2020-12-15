package advent

import java.util.*

fun solve15(scanner: Scanner): String {
    with(scanner) {
        val numbers = nextLine().split(",").map { it.toInt() }
        return findNumber2(numbers).toString()
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

fun findNumber2(numbers: List<Int>): Int {
    val spokenNumbers =
        numbers.mapIndexed { index, number ->
            number to Pair<Int, Int?>(index + 1, null)
        }.toMap().toMutableMap()
    var currentSpoken = numbers.last()
    var turn = numbers.size
    while (turn < 30000000) {
        turn++
        val last =
            spokenNumbers.getOrDefault(currentSpoken, Pair(turn - 1, null))
        currentSpoken = last.let {
            val whichTurn = it.first
            val whichPrevious = it.second ?: return@let 0
            return@let whichTurn - whichPrevious
        }
        spokenNumbers[currentSpoken] =
            Pair(turn, spokenNumbers.getOrDefault(currentSpoken, null)?.first)

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