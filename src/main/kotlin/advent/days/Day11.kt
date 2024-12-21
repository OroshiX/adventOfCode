package advent.days

import advent.DayPuzzle
import java.math.BigInteger
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

class Day11 : DayPuzzle<List<BigInteger>>() {
    override fun parse(scanner: Scanner): List<BigInteger> {
        return scanner.nextLine().split(" ").map { BigInteger(it) }
    }

    override fun solve1(input: List<BigInteger>): String {
        var current = input
        repeat(25) {
            current = current.nextStep()
        }
        return current.size.toString()
    }

    override fun solve2(input: List<BigInteger>): String {
        val inputMap = mutableMapOf<BigInteger, BigInteger>()
        for (item in input) {
            inputMap[item] = inputMap.getOrDefault(item, BigInteger.ZERO) + BigInteger.ONE
        }
        var current = inputMap.toMap()
        val nextNumbers = mutableMapOf<BigInteger, List<BigInteger>>()
        repeat(75) {
            current = nextStep(current, nextNumbers)
        }
        return current.values.sumOf { it }.toString()
    }
}

private val MULTIPLIER = BigInteger.valueOf(2024)

private fun List<BigInteger>.nextStep(): List<BigInteger> {
    val result = mutableListOf<BigInteger>()
    for (item in this) {
        if (item == BigInteger.ZERO) {
            result.add(BigInteger.ONE)
        } else {
            val digits = item.numberOfDigits()
            if (digits % 2 == 0) {
                // get first it digits of item
                (digits / 2).let {
                    val pow = BigInteger.valueOf(10.0.pow(it.toDouble()).toLong())
                    result.add(item / pow)
                    result.add(item % pow)
                }
            } else result.add(item * MULTIPLIER)
        }
    }
    return result
}

/**
 * Precondition: this > 0
 */
private fun BigInteger.numberOfDigits(): Int = (log10(this.toFloat()) + 1).toInt()

private fun BigInteger.nextNumbers(): List<BigInteger> {
    if (this == BigInteger.ZERO) {
        return listOf(BigInteger.ONE)
    }
    val digits = this.numberOfDigits()
    if (digits % 2 == 0) {
        // get first it digits of item
        (digits / 2).let {
            val pow = BigInteger.valueOf(10.0.pow(it.toDouble()).toLong())
            return listOf(this / pow, this % pow)
        }
    }
    return listOf(this * MULTIPLIER)
}

fun nextStep(
    inputToSum: Map<BigInteger, BigInteger>,
    nextNumbers: MutableMap<BigInteger, List<BigInteger>>
): Map<BigInteger, BigInteger> {
    val result = mutableMapOf<BigInteger, BigInteger>()
    for ((item, sum) in inputToSum) {
        val nextNumber = nextNumbers.getOrPut(item) { item.nextNumbers() }
        for (number in nextNumber) {
            result[number] = result.getOrDefault(number, BigInteger.ZERO) + sum
        }
    }
    return result
}
