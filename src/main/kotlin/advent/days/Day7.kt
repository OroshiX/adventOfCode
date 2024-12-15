package advent.days

import advent.DayPuzzle
import java.util.*
import kotlin.math.pow

class Day7 : DayPuzzle<List<Operation>>() {
    override fun parse(scanner: Scanner): List<Operation> {
        val operations = mutableListOf<Operation>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (line.isEmpty()) {
                break
            }
            val (expected, numbers) = line.split(": ")
            val expectedNumber = expected.toLong()
            val numbersList = numbers.split(" ").map { it.toLong() }
            operations.add(Operation(expectedNumber, numbersList))
        }
        return operations
    }

    override fun solve1(input: List<Operation>): String {
        var sum = 0L
        for (operation in input) {
            val solutions = operation.numberOfSolutions()
            if (solutions >= 1) {
                sum += operation.expected
            }
        }
        return sum.toString()
    }

    override fun solve2(input: List<Operation>): String {
        TODO()
    }
}

data class Operation(val expected: Long, val numbers: List<Long>) {
    fun numberOfSolutions(): Int {
        var sum = 0
        val size = numbers.size - 1
        val numberOfPossible = 2.0.pow(size).toInt()
        for (i in 0 until numberOfPossible) {
            val combination = i.toString(2).padStart(size, '0').map {
                if (it == '0') OperationType.ADDITION else OperationType.MULTIPLICATION
            }
            if (isCorrect(combination)) {
                sum++
            }
        }
        return sum
    }

    private fun isCorrect(operators: List<OperationType>): Boolean {
        var result = numbers[0]
        for (i in 0 until numbers.size - 1) {
            val number = numbers[i + 1]
            val operator = operators[i]
            when (operator) {
                OperationType.ADDITION -> result += number
                OperationType.MULTIPLICATION -> result *= number
            }
        }
        return result == expected
    }
}

enum class OperationType {
    ADDITION, MULTIPLICATION
}
