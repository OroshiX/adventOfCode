package advent

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.terminal.ConversionResult
import com.github.ajalt.mordant.terminal.StringPrompt
import com.github.ajalt.mordant.terminal.YesNoPrompt
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Paths
import java.util.*

private const val CONFIG = "config.properties"

/**
 * This class is used to configure the project.
 * We store the numDay and the part number here, as well as debug mode.
 *
 * Values are taken from the config.properties file, and stored back there when changed.
 */
object Config {
    private val directory = File(Paths.get("").toAbsolutePath().toString(), "inputs").apply {
        if (!exists()) mkdir()
    }
    private val file: File = File(directory, CONFIG)
    private const val KEY_NUM_DAY = "numDay"
    private const val KEY_PART = "part"
    private const val KEY_DEBUG = "debug"
    private const val KEY_SESSION = "session"

    private val props = Properties()

    init {
        if (file.exists()) {
            FileInputStream(file).use {
                props.load(it)
            }
        } else {
            file.createNewFile()
        }
    }

    var numDay: Int?
        get() = getProperty(KEY_NUM_DAY)?.toIntOrNull() ?: 1
        set(value) {
            value?.let { setProperty(KEY_NUM_DAY, it.toString()) }
        }
    var part: Part?
        get() = getProperty(KEY_PART)?.toIntOrNull()?.let { Part.entries[it - 1] }
        set(value) {
            value?.let { setProperty(KEY_PART, it.number.toString()) }
        }

    var debug: Boolean?
        get() = getProperty(KEY_DEBUG)?.toBoolean()
        set(value) {
            value?.let { setProperty(KEY_DEBUG, it.toString()) }
        }

    fun promptAllSet(): SnapshotProperties {
        var currentNumDay = numDay
        var currentPart = part
        var currentDebug = debug
        fun subtitle(day: Int, part: Part, debug: Boolean): String {
            return """
            Solving ${(TextStyles.bold + TextColors.brightRed + TextStyles.underline)("Day $day part ${part.number}")}${if (debug) " in debug mode" else ""}
            """.trimIndent()
        }
        if (currentNumDay != null && currentPart != null && currentDebug != null) {
            val subtitle = subtitle(currentNumDay, currentPart, currentDebug)
            terminal.println(subtitle)
            val shouldContinue = YesNoPrompt(
                """
            Continue without changing?
            """.trimIndent(),
                terminal,
                default = true,
                promptSuffix = ":\n"
            ).ask() ?: true
            if (shouldContinue) return SnapshotProperties(currentNumDay, currentPart, currentDebug)
        }

        currentNumDay = getNumDayAndPrompt()
        currentPart = getPartAndPrompt()
        currentDebug = getDebugAndPrompt()
        terminal.println(subtitle(currentNumDay, currentPart, currentDebug))
        return SnapshotProperties(currentNumDay, currentPart, currentDebug)
    }

    private fun expectedKey(numDay: Int, part: Part) = "expected.$numDay.${part.key}"
    fun getExpectedResult(numDay: Int, part: Part): String {
        return getProperty(expectedKey(numDay, part)).orEmpty().ifBlank {
            val expected =
                StringPrompt("expectedResult", terminal, allowBlank = false).ask().orEmpty()
            setProperty(expectedKey(numDay, part), expected)
            expected
        }
    }

    fun getSessionCookie(): String = getProperty(KEY_SESSION).orEmpty().ifBlank {
        val prompt = terminal.prompt("""
                No property named "session" in file ${(TextColors.black on TextColors.blue)("inputs > config.properties")}.
                Please get the value from your browser when accessing advent of code website by inspecting the webpage on ${
            (TextColors.black on TextColors.blue)(
                "chrome > Application > Cookies"
            )
        }, and paste the value here of the cookie named ${
            (TextColors.black on TextColors.blue)(
                "session"
            )
        }""".trimIndent(),
            promptSuffix = ":\n",
            convert = {
                if (it.isBlank()) ConversionResult.Invalid("Please enter a value for the session cookie")
                else ConversionResult.Valid(it)
            }).orEmpty()
        setProperty(KEY_SESSION, prompt)
        prompt
    }

    fun getPartAndPrompt(): Part {
        val partNumber = terminal.prompt(
            """
            Please enter the part to solve
            """.trimIndent(),
            promptSuffix = ":\n",
            choices = listOf(1, 2),
            default = part?.number,
            showDefault = true,
            showChoices = false,
            convert = {
                when (val num = it.toIntOrNull()) {
                    null -> ConversionResult.Invalid("Please enter a number")
                    1, 2 -> ConversionResult.Valid(num)
                    else -> ConversionResult.Invalid("Please enter a number between 1 and 2")
                }
            }
        ) ?: throw IllegalStateException("Part cannot be null")
        val part = when (partNumber) {
            1 -> Part.ONE
            2 -> Part.TWO
            else -> throw IllegalStateException("Part $partNumber is wrong")
        }
        this.part = part
        return part
    }

    fun getNumDayAndPrompt(): Int {
        val numDay = terminal.prompt(
            """
            Please enter the day number to solve
            """.trimIndent(),
            promptSuffix = ":\n",
            choices = (1..25).toList(),
            default = numDay,
            showDefault = true,
            showChoices = false,
            convert = {
                val num = it.toIntOrNull()
                if (num == null || num !in 1..25) ConversionResult.Invalid("Please enter a number between 1 and 25")
                else ConversionResult.Valid(num)
            }
        ) ?: throw IllegalStateException("Day cannot be null")
        this.numDay = numDay
        return numDay
    }

    fun getDebugAndPrompt(): Boolean {
        val debug = YesNoPrompt(
            """
            Do you want to enable debug mode?
            """.trimIndent(),
            terminal,
            default = debug,
            promptSuffix = ":\n"
        ).ask() ?: true
        this.debug = debug
        return debug
    }

    private fun getProperty(key: String): String? = props.getProperty(key)
    private fun setProperty(key: String, value: String) {
        props.setProperty(key, value)
        FileOutputStream(file).use {
            props.store(it, "Advent of Code Config")
        }
    }
}

data class SnapshotProperties(val numDay: Int, val part: Part, val debug: Boolean)
