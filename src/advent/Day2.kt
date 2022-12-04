package advent

import java.util.*

class Day2 : DayPuzzle<List<Pair<RPS, String>>>(2, false) {
    override fun parse(scanner: Scanner): List<Pair<RPS, String>> {
        with(scanner) {
            val input = mutableListOf<Pair<RPS, String>>()
            while (hasNextLine()) {
                val line = nextLine().split(" ")
                input.add(RPS.fromLetter(line[0]) to line[1])
            }
            return input
        }
    }

    override fun solve1(input: List<Pair<RPS, String>>): String {
        return input.sumOf { round ->
            RPS.fromLetter(round.second).roundScore(round.first)
        }.toString()
    }

    override fun solve2(input: List<Pair<RPS, String>>): String {
        return input.sumOf { round ->
            val resultNeeded = Result.fromLetter(round.second)
            val mySign = round.first.toGetResult(resultNeeded)
            mySign.roundScore(round.first)
        }.toString()
    }
}

enum class Result(val value: String) {
    Lose("X"), Draw("Y"), Win("Z");

    companion object {
        fun fromLetter(letter: String): Result = Result.values().first { it.value == letter }
    }
}

enum class RPS(private val score: Int) {
    Rock(1), Paper(2), Scissors(3);

    private fun against(other: RPS): Int {
        return when {
            this == other -> 3 // draw
            this.score == other.score - 1 || this.score == other.score + 2 -> 0 // lose
            else -> 6 // win
        }
    }

    fun toGetResult(result: Result): RPS {
        return when (this) {
            Rock -> when (result) {
                Result.Lose -> Scissors
                Result.Draw -> Rock
                Result.Win -> Paper
            }

            Paper -> when (result) {
                Result.Lose -> Rock
                Result.Draw -> Paper
                Result.Win -> Scissors
            }

            Scissors -> when (result) {
                Result.Lose -> Paper
                Result.Draw -> Scissors
                Result.Win -> Rock
            }
        }
    }

    fun roundScore(other: RPS): Int {
        return score + against(other)
    }

    companion object {
        fun fromLetter(letter: String): RPS = when (letter) {
            "A", "X" -> Rock
            "B", "Y" -> Paper
            "C", "Z" -> Scissors
            else -> throw IllegalArgumentException("Argument $letter is not valid")
        }
    }
}
