package advent.days

import advent.DayPuzzle
import java.util.*

class Day10 : DayPuzzle<List<LightDiagram>>() {
    override fun parse(scanner: Scanner): List<LightDiagram> {
        val input = mutableListOf<LightDiagram>()
        while (scanner.hasNext()) {
            val line = scanner.nextLine()
            val regex =
                """\[([.#]+)] ((\(\d+(,\d+)*\))+( (\(\d+(,\d+)*\)))*) \{(\d+(,\d+)*)}""".toRegex()

            regex.matchEntire(line)?.groupValues?.let { groups ->
                val fullTargets = groups[1].map { l -> l == '#' }.toList()
                val size = fullTargets.size
                val targets = mutableSetOf<Int>()
                for (i in 0 until size) {
                    if (fullTargets[i]) {
                        targets.add(i)
                    }
                }

                val buttonStrings = groups[2].split(" ")
                val listButtons = buttonStrings.map { representation ->
                    ButtonToggle(
                        representation.substring(1 until representation.length - 1).split(",")
                            .map { it.toInt() }
                    )
                }
                val joltage = groups[8].split(",").map { it.toInt() }
                input.add(
                    LightDiagram(
                        targetLightsOn = targets,
                        size = size,
                        buttonToggles = listButtons,
                        requiredJoltage = joltage
                    )
                )
            }
        }
        return input
    }

    override fun solve1(input: List<LightDiagram>): String {
        val sumPresses = input.sumOf { it.solveCount() }
        return sumPresses.toString()
    }

    override fun solve2(input: List<LightDiagram>): String {
        TODO()
    }
}

data class LightDiagram(
    val targetLightsOn: Set<Int>,
    val size: Int,
    val buttonToggles: List<ButtonToggle>,
    val requiredJoltage: List<Int>
) {
    fun solveCount(): Int {
        val buttonsPossible =
            targetLightsOn.forEach { lightPosition ->
                // TODO
            }
        return 0
    }
}

data class ButtonToggle(val positions: List<Int>) {
    fun toggle(indicatorLights: Set<Int>): Set<Int> {
        val indices = indicatorLights.union(positions)
        val newState = mutableSetOf<Int>()
        for (index in indices) {
            val current = indicatorLights.contains(index)
            val toggle = positions.contains(index)
            val new = current && !toggle || !current && toggle
            if (new) {
                newState.add(index)
            }
        }
        return newState
    }
}
