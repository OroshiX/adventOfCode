package advent

import com.github.ajalt.mordant.animation.textAnimation
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.rendering.TextStyles.underline
import com.github.ajalt.mordant.terminal.ConversionResult
import com.github.ajalt.mordant.terminal.StringPrompt
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Paths
import java.util.*


const val numDay = 1
const val year = 2023
val terminal = Terminal()
fun main() = runBlocking {
    val day = getDayByNumber(numDay)

    val title = """
        ❄️❄️❄️🌟❄️❄️❄️🌟❄️❄️❄️
        ${(green on black)("\uD83C\uDF84 Advent of Code $year \uD83C\uDF84")}
        ❄️❄️❄️🌟❄️❄️❄️🌟❄️❄️❄️
        
        Solving ${(bold + brightRed + underline)("Day $numDay part ${day.part.number}")}
        """.trimIndent()
    terminal.println("\n" + title)

    val scanner = Scanner(
        FileInputStream(
            if (day.debug) fileDebug(day.part)
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
    val res = day.solve(scanner)
    val elapsedTime = System.currentTimeMillis() - startTime
    if (day.debug) {
        val expected = expectedResult(day.part)
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

private fun fileDebug(part: Part): File {
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

private fun expectedResult(part: Part): String {
    val key = "expected.$numDay.${part.key}"
    return PropertiesReader.getProperty(key).orEmpty().ifBlank {
        val expected = StringPrompt("expectedResult", terminal, allowBlank = false).ask().orEmpty()
        PropertiesReader.setProperty(key, expected)
        expected
    }
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

    val sessionCookie = PropertiesReader.getProperty("session")

    val cookie = sessionCookie.orEmpty().ifBlank {
        val prompt = terminal.prompt("""
                    No property named "session" in file ${(black on blue)("inputs > config.properties")}.
                    Please get the value from your browser when accessing advent of code website by inspecting the webpage on ${
            (black on blue)(
                "chrome > Application > Cookies"
            )
        }, and paste the value here of the cookie named ${(black on blue)("session")}""".trimIndent(),
            promptSuffix = ":\n",
            convert = {
                if (it.isBlank()) ConversionResult.Invalid("Please enter a value for the session cookie")
                else ConversionResult.Valid(it)
            }).orEmpty()
        PropertiesReader.setProperty("session", prompt)
        prompt
    }

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


private const val CONFIG = "config.properties"

object PropertiesReader {
    private val properties = Properties()
    private val file: File = File(directory, CONFIG)

    init {
        if (!file.exists()) file.createNewFile()
        FileInputStream(file).use {
            properties.load(it)
        }
    }

    fun getProperty(key: String): String? = properties.getProperty(key)
    fun setProperty(key: String, value: String) {
        properties.setProperty(key, value)
        FileOutputStream(file).use {
            properties.store(it, "Cookies")
        }
    }
}
