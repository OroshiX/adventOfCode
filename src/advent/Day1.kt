package advent

import java.util.*

fun solve1(scanner: Scanner): String {
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
    for (v in list.withIndex()) {
        for (j in v.index + 1 until list.size) {
            if (v.value + list[j] == 2020) return v.value * list[j]
        }
    }
    return null
}

fun findPart2(list: List<Int>): Int? {
    for (v in list.withIndex()) {
        for (j in v.index + 1 until list.size) {
            for (k in j + 1 until list.size) {
                if (v.value + list[j] + list[k] == 2020) return v.value * list[j] * list[k]
            }
        }
    }
    return null
}