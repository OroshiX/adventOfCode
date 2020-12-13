package advent

import java.io.FileInputStream
import java.util.*

fun main(args: Array<String>) {
    val numDay = 2
    val function = ::solve2
    val startTime = System.currentTimeMillis()
    val scanner =
        Scanner(FileInputStream("D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\inputDay$numDay.txt"))
    val res = function.invoke(scanner)
    System.err.println("Took ${(System.currentTimeMillis() - startTime) / 1000} s to execute")
    print(res)
}