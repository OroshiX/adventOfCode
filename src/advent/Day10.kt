package advent

import java.util.*

fun solve10(scanner: Scanner): String {
    with(scanner) {
        var adapters = mutableListOf<Int>()
        while (hasNext()) {
            val n = nextLine().toInt()
            adapters.add(n)
        }
        adapters = adapters.sorted().toMutableList()
        // add last
        adapters.add(adapters.last() + 3)
        return calculateDifferences(adapters).toString()
    }
}

fun calculateDifferences(sorted: List<Int>): Int {
    var diff1 = 0
    var diff3 = 0
    var current = 0
    for (adapter in sorted) {
        when (adapter - current) {
            1 -> diff1++
            3 -> diff3++
        }
        current = adapter
    }
    return diff1 * diff3
}
