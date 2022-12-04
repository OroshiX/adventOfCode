package advent

import java.util.*

sealed class DayPuzzle<T>(val numDay: Int, val debug: Boolean) {
    fun solve(part: Int, scanner: Scanner): String {
        return when (part) {
            1 -> solve1(parse(scanner))
            2 -> solve2(parse(scanner))
            else -> throw IllegalArgumentException("Part $part does not exist")
        }
    }

    abstract fun parse(scanner: Scanner): T
    abstract fun solve1(input: T): String
    abstract fun solve2(input: T): String
}
