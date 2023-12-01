package advent

import java.util.*

sealed class DayPuzzle<T>(val part: Part, val debug: Boolean) {
    fun solve(scanner: Scanner): String {
        return when (part) {
            Part.ONE -> solve1(parse(scanner))
            Part.TWO -> solve2(parse(scanner))
        }
    }

    abstract fun parse(scanner: Scanner): T
    abstract fun solve1(input: T): String
    abstract fun solve2(input: T): String
    abstract fun expectedDebug1(): String
    abstract fun expectedDebug2(): String
}

enum class Part {
    ONE, TWO
}