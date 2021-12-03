package advent

import java.util.*

fun solve3(scanner: Scanner): String {
    val lines = mutableListOf<String>()
    while (scanner.hasNext()) {
        lines.add(scanner.nextLine())
    }
    val (gamma, epsilon) = gammaAndEpsilonRate(lines)

    return (gamma * epsilon).toString()
}

fun gammaAndEpsilonRate(list: List<String>): Pair<Int, Int> {
    val half = list.size / 2
    val count = MutableList(list.first().length) { 0 }
    val gamma = StringBuilder()
    val epsilon = StringBuilder()
    for (j in 0 until list.first().length) {
        for (i in list.indices) {
            val c = list[i][j]
            if (c == '1') {
                count[j]++
            }
            if (count[j] >= half) {
                break
            }
        }
        if (count[j] >= half) {
            gamma.append('1')
            epsilon.append('0')
        } else {
            gamma.append('0')
            epsilon.append('1')
        }
    }
    return Pair(Integer.parseInt(gamma.toString(), 2), Integer.parseInt(epsilon.toString(), 2))
}
