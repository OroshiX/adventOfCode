package advent.days

import advent.DayPuzzle
import advent.service.Progress
import java.util.*

class Day6 : DayPuzzle<List<MathProblem>>() {
    override fun parse(scanner: Scanner): List<MathProblem> {
        val lines = mutableListOf<String>()
        while (scanner.hasNext()) {
            val line = scanner.nextLine()
            lines.add(line)
        }
        val size = lines.first().split(" ").filter { it.isNotBlank() }.size
        val numbers = List(size) { List(lines.size - 1) { -1L }.toMutableList() }.toMutableList()
        val operators = List(size) { Operation.Add }.toMutableList()

        for (i in lines.indices) {
            val line = lines[i]
            if (i == lines.lastIndex) {
                // operator
                line.split(" ").filterNot { it.isBlank() }.forEachIndexed { j, operation ->
                    operators[j] = Operation.from(operation)
                }
            } else {
                // numbers
                line.split(" ").filterNot { it.isBlank() }.forEachIndexed { j, number ->
                    numbers[j][i] = number.toLong()
                }
            }
        }
        return List(size) {
            MathProblem(numbers = numbers[it], operation = operators[it])
        }
    }

    override fun solve1(input: List<MathProblem>, onProgressUpdate: suspend (Progress) -> Unit): String {
        var sum = 0L
        for (mathProblem in input) {
            sum += mathProblem.solve()
        }
        return sum.toString()
    }

    override fun solve2(input: List<MathProblem>, onProgressUpdate: suspend (Progress) -> Unit): String {
        TODO()
    }
}

data class MathProblem(val numbers: List<Long>, val operation: Operation) {
    fun solve(): Long {
        var res = numbers.first()
        for (index in 1 until numbers.size) {
            val number = numbers[index]
            when (operation) {
                Operation.Add -> {
                    res += number
                }

                Operation.Multiply -> {
                    res *= number
                }
            }
        }
        return res
    }
}

enum class Operation(val char: String) {
    Add("+"),
    Multiply("*");

    companion object {
        fun from(char: String): Operation {
            return entries.first { it.char == char }
        }
    }
}
