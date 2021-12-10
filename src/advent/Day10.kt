package advent

import java.util.*

fun solve10(scanner: Scanner): String {
    with(scanner) {
        val lines = mutableListOf<String>()
        while (hasNext()) {
            lines += nextLine()
        }
        return lines.sumOf { it.illegalScore() }.toString()
    }
}

fun String.illegalScore(): Long {
    val stack = Stack<Char>()
    for (c in this) {
        when (c) {
            '(', '[', '{', '<' -> stack.push(c)
            else -> {
                if (stack.pop() != c.matchingOpen()) {
                    return c.score()
                }
            }
        }
    }
    return 0
}

fun Char.matchingOpen(): Char {
    return when (this) {
        ')' -> '('
        ']' -> '['
        '}' -> '{'
        '>' -> '<'
        else -> throw IllegalStateException()
    }
}

fun Char.score(): Long {
    return when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalStateException()
    }
}