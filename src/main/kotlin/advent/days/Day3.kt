package advent.days

import advent.DayPuzzle
import java.util.*

class Day3 : DayPuzzle<String>() {
    override fun parse(scanner: Scanner): String {
        var line = ""
        while (scanner.hasNextLine()) {
            line += scanner.nextLine() + "\n"
        }
        return line.substringBeforeLast("\n")
    }

    override fun solve1(input: String): String {
        val multipliers = input.toMultipliers()
        return multipliers.sumOf { it.multiply() }.toString()
    }

    override fun solve2(input: String): String {
        val dos = """do\(\)""".toRegex().findAll(input).iterator()
        val donts = """don't\(\)""".toRegex().findAll(input).iterator()
        val switches = mutableListOf<Switch>()
        dos.forEach {
            switches.add(Switch(true, it.range))
        }
        donts.forEach {
            switches.add(Switch(false, it.range))
        }
        switches.sortBy { it.position.first }
        var on = true
        var sum = 0
        var i = 0
        for (switch in switches) {
            if (on) {
                val sub = input.substring(i, switch.position.first)
                sum += sub.toMultipliers().sumOf { it.multiply() }
            }
            on = switch.on
            i = switch.position.last + 1
        }
        if (on) {
            val sub = input.substring(i)
            sum += sub.toMultipliers().sumOf { it.multiply() }
        }
        return sum.toString()
    }
}

private data class Switch(val on: Boolean, val position: IntRange)

private data class Multiply(val a: Int, val b: Int) {
    fun multiply(): Int = a * b
}

private fun String.toMultipliers(): List<Multiply> =
    """mul\((?<a>\d{1,3}),(?<b>\d{1,3})\)""".toRegex().findAll(this).map {
        val (a, b) = it.destructured
        Multiply(a.toInt(), b.toInt())
    }.toList()
