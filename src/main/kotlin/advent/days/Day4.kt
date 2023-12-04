package advent.days

import advent.DayPuzzle
import java.util.*
import kotlin.math.pow

class Day4 : DayPuzzle<List<Card>>() {
    override fun parse(scanner: Scanner): List<Card> {
        val cards = mutableListOf<Card>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (line.isBlank()) break
            val (id, winning, mine) = line.split("Card ", ": ", " | ")
                .filter { it.isNotBlank() }
            val winningNumbers = winning.trim().split(" +".toRegex())
                .filter { it.isNotBlank() }.map { it.toInt() }
            val myNumbers = mine.trim().split(" +".toRegex())
                .filter { it.isNotBlank() }.map { it.toInt() }
            cards.add(Card(id.trim().toInt(), winningNumbers, myNumbers))
        }
        return cards
    }

    override fun solve1(input: List<Card>): String = input.sumOf { it.score }.toString()

    override fun solve2(input: List<Card>): String {
        var totalCards = 0
        val lastId = input.last().id
        val idToWinningSize = input.associate { it.id to (it.winningCardSize to 1) }.toMutableMap()
        for (i in 1..lastId) {
            val (winningSize, quantity) = idToWinningSize.getValue(i)
            totalCards += quantity
            if (winningSize == 0) continue
            for (j in i + 1..i + winningSize) {
                val (_, qty) = idToWinningSize.getValue(j)
                idToWinningSize[j] = idToWinningSize.getValue(j).copy(second = qty + quantity)
            }
        }
        return totalCards.toString()
    }
}

data class Card(val id: Int, val winningNumbers: List<Int>, val myNumbers: List<Int>) {
    val winningCardSize: Int = winningNumbers.intersect(myNumbers.toSet()).size
    val score: Long = 2f.pow(winningCardSize - 1).toLong()
}