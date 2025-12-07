package advent

import advent.days.*
import com.github.ajalt.mordant.animation.textAnimation
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.rendering.TextStyles.underline
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.danger
import com.github.ajalt.mordant.terminal.info
import com.github.ajalt.mordant.terminal.success
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Paths
import java.util.*


//const val numDay = 1
const val year = 2024
val terminal = Terminal()
fun main() = runBlocking {
    val title = """
        ❄️❄️❄️🌟❄️❄️❄️🌟❄️❄️❄️
        ${(green on black)("\uD83C\uDF84 Advent of Code $year \uD83C\uDF84")}
        ❄️❄️❄️🌟❄️❄️❄️🌟❄️❄️❄️
        
        """.trimIndent()
    terminal.println("\n" + title)

    // Prompt user to get the day to solve and the part
    val (numDay, part, debug) = Config.promptAllSet()

    val day = getDayByNumber(numDay)

    val scanner = Scanner(
        FileInputStream(
            if (debug) fileDebug(numDay, part)
            else saveFile(numDay, year)
        )
    )
    val anim = terminal.textAnimation<Int> { frame ->
        (1..50).joinToString("") {
            val hue = (frame + it) * 3 % 360
            TextColors.hsv(hue, 1, 1)("===")
        }
    }
    terminal.cursor.hide(showOnExit = true)
    var finished = false
    launch {
        var i = 0
        while (finished.not()) {
            anim.update(i)
            i++
            delay(50)
        }
    }

    val startTime = System.currentTimeMillis()
    val res = day.solve(scanner, part)
    val elapsedTime = System.currentTimeMillis() - startTime
    if (debug) {
        val expected = Config.getExpectedResult(numDay, part)
        if (expected == res) {
            terminal.success("✅ Debug result is okay")
        } else {
            terminal.danger("❌ Expected $expected but got $res")
        }
    }
    finished = true
    terminal.info("Took $elapsedTime ms to execute\n")

    terminal.println("${underline("Result is:")}\n${(bold + (brightCyan on black))(res)}")
}

private val directory = File(Paths.get("").toAbsolutePath().toString(), "inputs").apply {
    if (!exists()) mkdir()
}

private fun fileDebug(numDay: Int, part: Part): File {
    val file = File(directory, "t$numDay-${part.key}")
    if (file.exists()) return file
    file.createNewFile()

    terminal.info("What debug input would you like to test? Paste the debug input here, and end with \"/////\" (it's 5 '/'), and press enter")
    val lines = mutableListOf<String>()
    var line = readln()
    while (line != "/////") {
        lines += line
        line = readln()
    }
    FileOutputStream(file, true).use {
        it.write(lines.joinToString("\n").toByteArray(Charsets.UTF_8))
    }
    return file
}

@Suppress("SameParameterValue")
private fun getDayByNumber(numDay: Int): DayPuzzle<*> = when (numDay) {
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
    else -> throw IllegalArgumentException("Day $numDay is not a valid day")
}

fun saveFile(num: Int, year: Int): File {
    val file = File(directory, "inputDay$num.txt")

    if (file.exists()) return file

    val cookie = Config.getSessionCookie()

    download(
        "https://adventofcode.com/$year/day/$num/input",
        file, "session=$cookie"
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

