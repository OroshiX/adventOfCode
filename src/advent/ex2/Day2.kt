package advent.ex2

import java.util.*

fun solve(scanner: Scanner): String {
    with(scanner) {
        val list = mutableListOf<Int>()
        while (this.hasNext()) {
            val n = nextLine().toInt()
            list.add(n)
        }
        return findPart2(list).toString()
    }
}

fun findPart1(list: List<Int>): Int? {

    return null
}

fun findPart2(list: List<Int>): Int? {

    return null
}