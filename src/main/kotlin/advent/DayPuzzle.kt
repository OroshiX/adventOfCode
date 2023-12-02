package advent

import java.util.*

abstract class DayPuzzle<T>(val part: Part, val debug: Boolean) {
    fun solve(scanner: Scanner): String {
        return when (part) {
            Part.ONE -> solve1(parse(scanner))
            Part.TWO -> solve2(parse(scanner))
        }
    }

    abstract fun parse(scanner: Scanner): T
    abstract fun solve1(input: T): String
    abstract fun solve2(input: T): String
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
}
