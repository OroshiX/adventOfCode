package advent

import java.util.*
import kotlin.math.roundToInt

fun solve3(scanner: Scanner): String {
    val lines = mutableListOf<String>()
    while (scanner.hasNext()) {
        lines.add(scanner.nextLine())
    }
    val (oxygen, co2) = oxygenCo2(lines)
    return (oxygen * co2).toString()
}

fun mostCommonAt(list: List<String>, j: Int, modeMost: Boolean = true): Char {
    val half = (list.size.toFloat() / 2).roundToInt()
    var count = 0
    for (i in list.indices) {
        if (list[i][j] == '1') {
            count++
        }
        if (count >= half) {
            break
        }
    }
    return if(modeMost) {
        if(count >= half) '1' else '0'
    } else {
        if(count >= half) '0' else '1'
    }
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

fun oxygenCo2(list: List<String>): Pair<Int, Int> {
    var oxyList = list.toList()
    var co2List = list.toList()
    var j = 0
    while (oxyList.size > 1) {
        val most = mostCommonAt(oxyList, j)
        oxyList = oxyList.filterBy(most, j)
        j++
    }
    j = 0
    while (co2List.size > 1) {
        val least = mostCommonAt(co2List, j, false)
        co2List = co2List.filterBy(least, j)
        j++
    }
    return Pair(Integer.parseInt(oxyList.first(), 2), Integer.parseInt(co2List.first(), 2))
}

fun List<String>.filterBy(char: Char, index: Int): List<String> {
    return filter { it[index] == char }
}