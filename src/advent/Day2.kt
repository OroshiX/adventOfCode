package advent

import java.util.*

class Day2 : DayPuzzle<List<Pair<RPS, RPS>>>(2, false) {
    override fun parse(scanner: Scanner): List<Pair<RPS, RPS>> {
        with(scanner) {
            val input = mutableListOf<Pair<RPS, RPS>>()
            while (hasNextLine()) {
                val line = nextLine().split(" ")
                input.add(RPS.fromLetter(line[0]) to RPS.fromLetter(line[1]))
            }
            return input
        }
    }

    override fun solve1(input: List<Pair<RPS, RPS>>): String {
        return input.sumOf { round -> round.second.roundScore(round.first) }.toString()
    }

    override fun solve2(input: List<Pair<RPS, RPS>>): String {
        TODO("Not yet implemented")
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
