package advent

import advent.service.Progress
import java.util.Scanner

abstract class DayPuzzle<T> {
    fun solve(scanner: Scanner, part: Part, onProgressUpdate: suspend (Progress) -> Unit): String {
        return when (part) {
            Part.ONE -> solve1(parse(scanner), onProgressUpdate)
            Part.TWO -> solve2(parse(scanner), onProgressUpdate)
        }
    }

    abstract fun parse(scanner: Scanner): T
    abstract fun solve1(input: T, onProgressUpdate: suspend (Progress) -> Unit): String
    abstract fun solve2(input: T, onProgressUpdate: suspend (Progress) -> Unit): String
}

enum class Part {
    ONE, TWO;

    val key: String
        get() = when (this) {
            ONE -> "part1"
            TWO -> "part2"
        }

    val number: Int
        get() = when (this) {
            ONE -> 1
            TWO -> 2
        }
    val display: String
        get() = when (this) {
            ONE -> "Partie 1"
            TWO -> "Partie 2"
        }

    companion object {
        fun fromNumber(number: Int): Part? {
            return Part.entries.firstOrNull { part -> part.number == number }
        }
    }
}
