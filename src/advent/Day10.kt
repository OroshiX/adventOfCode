package advent

import java.util.*

fun solve10(scanner: Scanner): String {
    with(scanner) {
        val lines = mutableListOf<String>()
        while (hasNext()) {
            lines += nextLine()
        }
        val autoComplete = lines.map { it.autoComplete() }.toMutableList()
        val res = autoComplete.filter { it != 0L }.sorted()
        return res[res.size / 2].toString()
    }
}

private fun String.autoComplete(): Long {
    val stack = Stack<Char>()
    for (c in this) {
        when (c) {
            '(', '[', '{', '<' -> stack.push(c)
            else -> {
                if (stack.pop() != c.matchingOpen()) {
                    return 0
                }
            }
        }
    }
    var score = 0L
    while (stack.isNotEmpty()) {
        score *= 5
        score += stack.pop().autoCompleteScore()
    }
    return score

}

private fun Char.autoCompleteScore(): Long {
    return when (this) {
        '(' -> 1
        '[' -> 2
        '{' -> 3
        '<' -> 4
        else -> throw  IllegalStateException()
    }
}

private fun String.illegalScore(): Long {
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

private fun Char.matchingOpen(): Char {
    return when (this) {
        ')' -> '('
        ']' -> '['
        '}' -> '{'
        '>' -> '<'
        else -> throw IllegalStateException()
    }
}

private fun Char.score(): Long {
    return when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalStateException()
    }
}