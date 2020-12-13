package advent

import java.io.FileInputStream
import java.util.*

fun main(args: Array<String>) {
    val numDay = 1
    val startTime = System.currentTimeMillis()
    val res =
        solve(Scanner(FileInputStream("D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\inputDay$numDay.txt")))
    System.err.println("Took ${(System.currentTimeMillis() - startTime) / 1000} s to execute")
    print(res)
}

fun solve(scanner: Scanner): String {
    with(scanner) {
        val list = mutableListOf<Int>()
        while (this.hasNext()) {
            val n = nextLine().toInt()
            list.add(n)
        }
        return find(list).toString()
    }
}

fun find(list: List<Int>): Int? {
    for (v in list.withIndex()) {
        for (j in v.index + 1 until list.size) {
            if (v.value + list[j] == 2020) return v.value * list[j]
        }
    }
    return null
}