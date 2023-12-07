package advent.days

import advent.DayPuzzle
import java.util.*

class Day7 : DayPuzzle<List<CamelHand>>() {
    override fun parse(scanner: Scanner): List<CamelHand> {
        val output = mutableListOf<CamelHand>()
        while (scanner.hasNextLine()) {
            val (hand, bid) = scanner.nextLine().split(" +".toRegex())
            output += CamelHand(hand, bid.trim().toLong())
        }
        return output
    }

    override fun solve1(input: List<CamelHand>): String {
        val sorted = input.sortedWith(Compare(order1) {
            it.handType
        })
        val res = sorted.score()
        return res.toString()
    }

    override fun solve2(input: List<CamelHand>): String {
        val sorted = input.sortedWith(Compare(order2) {
            it.handType2
        })
        val res = sorted.score()
        return res.toString()
    }

    private fun List<CamelHand>.score(): Long = foldIndexed(0L) { index, acc, hand -> acc + hand.bid * (index + 1) }
}

class Compare(private val cardOrder: List<Char>, val handType: (CamelHand) -> HandType) : Comparator<CamelHand> {
    override fun compare(hand0: CamelHand, hand1: CamelHand): Int {
        if (handType(hand0).order != handType(hand1).order) return handType(hand0).order.compareTo(handType(hand1).order)

        for (i in 0 until 5) {
            if (hand0.hand[i] == hand1.hand[i]) continue
            val index0 = cardOrder.indexOf(hand0.hand[i])
            val index1 = cardOrder.indexOf(hand1.hand[i])
            return index1.compareTo(index0)
        }
        return 0
    }

}

data class CamelHand(val hand: String, val bid: Long) {
    val handType: HandType
    val handType2: HandType

    init {
        val map = hand.fold(mutableMapOf<Char, Int>()) { acc, c ->
            acc[c] = acc.getOrDefault(c, 0) + 1
            acc
        }
        var values = map.values
        handType = when {
            values.contains(5) -> HandType.FIVE
            values.contains(4) -> HandType.FOUR
            values.contains(3) && map.values.contains(2) -> HandType.FULL_HOUSE
            values.contains(3) -> HandType.THREE
            values.contains(2) && values.count { it == 2 } == 2 -> HandType.TWO_PAIRS
            values.contains(2) && values.count { it == 2 } == 1 -> HandType.ONE_PAIR
            map.size == 5 -> HandType.HIGH_CARD
            else -> throw IllegalArgumentException()
        }
        val numberJoker = map['J'] ?: 0
        map.remove('J')
        values = map.values
        handType2 = when {
            numberJoker == 0 -> handType
            numberJoker == 5 || numberJoker == 4 -> HandType.FIVE
            numberJoker == 3 -> if (values.contains(2)) HandType.FIVE else HandType.FOUR
            numberJoker == 2 && values.max() == 1 -> HandType.THREE
            numberJoker == 2 && values.max() == 2 -> HandType.FOUR
            numberJoker == 2 && values.max() == 3 -> HandType.FIVE
            numberJoker == 1 && values.max() == 1 -> HandType.ONE_PAIR
            numberJoker == 1 && values.count { it == 2 } == 1 -> HandType.THREE
            numberJoker == 1 && values.count { it == 2 } == 2 -> HandType.FULL_HOUSE
            numberJoker == 1 && values.contains(3) -> HandType.FOUR
            numberJoker == 1 && values.contains(4) -> HandType.FIVE
            else -> throw IllegalArgumentException()
        }
    }
}

private val order1 = "AKQJT98765432".toList()
private val order2 = "AKQT98765432J".toList()

enum class HandType(val order: Int) {
    FIVE(7), FOUR(6), FULL_HOUSE(5), THREE(4), TWO_PAIRS(3), ONE_PAIR(2), HIGH_CARD(1)
}