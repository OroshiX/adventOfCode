package advent

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.ConversionResult
import com.github.ajalt.mordant.terminal.prompt


/**
 * This class is used to configure the project.
 * We store the numDay and the part number here, as well as debug mode.
 *
 * Values are taken from the config.properties file, and stored back there when changed.
 */
object Config {
    init {
        val prompt = terminal.prompt(
            """
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
            })
    }
}

data class SnapshotProperties(val numDay: Int, val part: Part, val debug: Boolean)
