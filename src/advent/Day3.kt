package advent

import java.util.*

fun solve3(scanner: Scanner): String {
    with(scanner) {
        val grid = mutableListOf<String>()
        while (hasNext()) {
            val line = nextLine()
            grid.add(line)
        }
        return numberOfTrees2(grid).toString()
    }
}

fun numberOfTrees1(grid: List<String>): Int {
    var sum = 0
    var j = 0
    val length = grid.first().length
    for (l in grid) {
        if (l[j] == '#') {
            sum++
        }
        j = (j + 3) % length
    }
    return sum
}

fun numberOfTrees2(grid: List<String>): Int {
    var sum1 = 0
    var sum2 = 0
    var sum3 = 0
    var sum4 = 0
    var sum5 = 0
    var j1 = 0
    var j2 = 0
    var j3 = 0
    var j4 = 0
    var j5 = 0
    val length = grid.first().length
    for (l in grid.withIndex()) {
        if (l.value[j1] == '#') sum1++
        if (l.value[j2] == '#') sum2++
        if (l.value[j3] == '#') sum3++
        if (l.value[j4] == '#') sum4++
        if (l.index % 2 == 0) {
            if (l.value[j5] == '#') sum5++
            j5 = (j5 + 1) % length
        }
        j1 = (j1 + 1) % length
        j2 = (j2 + 3) % length
        j3 = (j3 + 5) % length
        j4 = (j4 + 7) % length
    }
    return sum1 * sum2 * sum3 * sum4 * sum5
}
