package advent

import java.util.*

fun solve1(scanner: Scanner): String {
    with(scanner) {
        val input = mutableListOf<Int>()
        while (hasNext()) {
            input.add(nextLine()!!.toInt())
        }
        println(input)
        return findPart1(input).toString()
    }
}

fun findPart1(list: List<Int>): Int {
    var prev = Int.MAX_VALUE
    var res = 0
    for (mes in list) {
        if (mes > prev) {
            res++
        }
        prev = mes
    }
    return res
}

fun findPart2(list: List<Int>): Int? {
    TODO()
}