package advent

import java.util.*

fun solve13(scanner: Scanner): String {
    with(scanner) {
        val earliest = nextLine().toInt()
        val buses = nextLine().split(",").toMutableList()
            .apply { removeIf { it == "x" } }.map { it.toInt() }

        return earliestResult(earliest, buses).toString()
    }
}

fun earliestResult(earliest: Int, buses: List<Int>): Int {
    var time = earliest
    val busId: Int
    l@ while (true) {
        for (id in buses) {
            if (time % id == 0) {
                busId = id
                break@l
            }
        }
        time++
    }
    return (time - earliest) * busId
}
