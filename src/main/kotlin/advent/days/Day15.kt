package advent.days

import advent.DayPuzzle
import java.util.*

class Day15 : DayPuzzle<List<String>>() {
    override fun parse(scanner: Scanner): List<String> {
        return scanner.nextLine().split(",")
    }

    override fun solve1(input: List<String>): String {
        return input.sumOf { hash(it) }.toString()
    }

    private fun hash(sequence: String): Int {
        var hash = 0
        for (c in sequence) {
            hash = ((hash + c.code) * 17) % 256
        }
        return hash
    }

    override fun solve2(input: List<String>): String {
        TODO("Not yet implemented")
    }

}
