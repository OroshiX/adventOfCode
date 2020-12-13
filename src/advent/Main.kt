package advent

import java.io.FileInputStream
import java.util.*

fun main(args: Array<String>) {
    val debug: Boolean = false
    val numDay = 4
    val function = ::solve4
    val startTime = System.currentTimeMillis()
    val scanner =
        Scanner(FileInputStream(if (debug) "D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\t" else "D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\inputDay$numDay.txt"))
    val res = function.invoke(scanner)
    System.err.println("Took ${(System.currentTimeMillis() - startTime) / 1000} s to execute")
    print(res)
}