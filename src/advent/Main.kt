package advent

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.util.*


fun main() {
    val numDay = 4
    val part = 1
    val year = 2022
    val day = getDayByNumber(numDay)
    val startTime = System.currentTimeMillis()
    val scanner = Scanner(
        FileInputStream(
            if (day.debug) "D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\t"
            else saveFile(numDay, year)
        )
    )
    val res = day.solve(part, scanner)
    System.err.println(
        "Took ${(System.currentTimeMillis() - startTime)} ms to execute"
    )
    println(res)
}

private fun getDayByNumber(num: Int): DayPuzzle<*> {
    return when (num) {
        1 -> Day1()
        2 -> Day2()
        3 -> Day3()
        4 -> Day4()
        5 -> Day5()
        6 -> Day6()
        7 -> Day7()
        8 -> Day8()
        9 -> Day9()
        10 -> Day10()
        11 -> Day11()
        12 -> Day12()
        13 -> Day13()
        14 -> Day14()
        15 -> Day15()
        16 -> Day16()
        17 -> Day17()
        18 -> Day18()
        19 -> Day19()
        20 -> Day20()
        21 -> Day21()
        22 -> Day22()
        23 -> Day23()
        24 -> Day24()
        25 -> Day25()
        else -> throw IllegalArgumentException("Day $num is not a valid day")
    }
}

fun saveFile(num: Int, year: Int): String {
    val path =
        "D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\inputDay$num.txt"

    val file = File(path)
    if (file.exists())
        return path
    download(
        "https://adventofcode.com/$year/day/$num/input",
        file, System.getenv("cookie")
    )
    return path
}

fun download(link: String, file: File, cookie: String) {
    val connection = URL(link).openConnection()
    connection.setRequestProperty("Cookie", cookie)
    connection.connect()
    connection.getInputStream().use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
}
