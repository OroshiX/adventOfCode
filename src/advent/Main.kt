package advent

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Paths
import java.util.*


fun main() {
    val numDay = 1
    val year = 2022
    val day = getDayByNumber(numDay)
    val startTime = System.currentTimeMillis()
    val scanner = Scanner(
        FileInputStream(
            if (day.debug) fileDebug
            else saveFile(numDay, year)
        )
    )
    val res = day.solve(scanner)
    System.err.println(
        "Took ${(System.currentTimeMillis() - startTime)} ms to execute"
    )
    if (day.debug) {
        val (resultOkay, expected) = when (day.part) {
            Part.ONE -> (day.expectedDebug1() == res) to day.expectedDebug1()
            Part.TWO -> (day.expectedDebug2() == res) to day.expectedDebug2()
        }
        println(if (resultOkay) "✅ Debug result is okay\n" else "❌ Expected $expected but got $res\n")
    }
    println("Result is:\n$res")
}

private val directory = File(Paths.get("").toAbsolutePath().toString(), "inputs").apply {
    if (!exists())
        mkdir()
}
private val fileDebug: File by lazy {
    File(directory, "t").apply {
        if (!exists())
            createNewFile()
    }
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

fun saveFile(num: Int, year: Int): File {
    val file = File(directory, "inputDay$num.txt")

    if (file.exists())
        return file
    download(
        "https://adventofcode.com/$year/day/$num/input",
        file, System.getenv("cookie")
    )
    return file
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
