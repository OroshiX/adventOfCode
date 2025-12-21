package advent

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.runBlocking


//const val numDay = 1
const val year = 2025
val terminal = Terminal()
val title = """
        ❄️❄️❄️🌟❄️❄️❄️🌟❄️❄️❄️
        ${(green on black)("\uD83C\uDF84 Advent of Code $year \uD83C\uDF84")}
        ❄️❄️❄️🌟❄️❄️❄️🌟❄️❄️❄️
        
        """.trimIndent()

fun main1() = runBlocking {
    terminal.println("\n" + title)
}
