package advent.days

import advent.DayPuzzle
import java.util.*

class Day13 : DayPuzzle<List<ClawMachine>>() {
    override fun parse(scanner: Scanner): List<ClawMachine> {
        val clawMachines = mutableListOf<ClawMachine>()
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine()

            val regexA = """Button A: X\+(?<x>\d+), Y\+(?<y>\d+)""".toRegex()
            // get the group value x and y for matchA
            val matchA = regexA.find(line)
            val xA = matchA?.groups?.get("x")?.value?.toLong() ?: 0
            val yA = matchA?.groups?.get("y")?.value?.toLong() ?: 0

            line = scanner.nextLine()
            val regexB = """Button B: X\+(?<x>\d+), Y\+(?<y>\d+)""".toRegex()
            // get the group value x and y for matchB
            val matchB = regexB.find(line)
            val xB = matchB?.groups?.get("x")?.value?.toLong() ?: 0
            val yB = matchB?.groups?.get("y")?.value?.toLong() ?: 0

            line = scanner.nextLine()
            val regexPrize = """Prize: X=(?<x>\d+), Y=(?<y>\d+)""".toRegex()
            // get the group value x and y for matchPrize
            val matchPrize = regexPrize.find(line)
            val xPrize = matchPrize?.groups?.get("x")?.value?.toLong() ?: 0
            val yPrize = matchPrize?.groups?.get("y")?.value?.toLong() ?: 0

            if (scanner.hasNextLine()) {
                scanner.nextLine()
            }

            clawMachines.add(
                ClawMachine(
                    buttonA = XY(xA, yA),
                    buttonB = XY(xB, yB),
                    prize = XY(xPrize, yPrize),
                )
            )
        }
        return clawMachines
    }

    override fun solve1(input: List<ClawMachine>): String {
        return input.sumOf { it.nbTokens(part1 = true) }.toString()
    }

    override fun solve2(input: List<ClawMachine>): String {
        return input.sumOf { it.nbTokens(part1 = false) }.toString()
    }
}

data class XY(val x: Long, val y: Long)

data class ClawMachine(val buttonA: XY, val buttonB: XY, val prize: XY) {
    private fun myPrize(part1: Boolean): XY = if (part1) prize else XY(prize.x + 10000000000000, prize.y + 10000000000000)

    fun hasMultipleSolutions(): Boolean {
        return dividende == 0L
    }

    private val dividende = buttonA.x * buttonB.y - buttonA.y * buttonB.x

    private fun solutionAB(part1: Boolean): Pair<Long, Long>? {
        if ((myPrize(part1).x * buttonB.y - myPrize(part1).y * buttonB.x) % dividende != 0L ||
            (myPrize(part1).y * buttonA.x - myPrize(part1).x * buttonA.y) % dividende != 0L
        ) {
            return null
        }
        val b = (myPrize(part1).y * buttonA.x - myPrize(part1).x * buttonA.y) / dividende
        val a = (myPrize(part1).x - b * buttonB.x) / buttonA.x
        return Pair(a, b)
    }

    fun nbTokens(part1: Boolean): Long {
        val solution = solutionAB(part1)
        if (solution != null) {
            val (a, b) = solution
            return 3 * a + b
        }
        return 0
    }

}
