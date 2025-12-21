package advent.days

import advent.DayPuzzle
import advent.service.Progress
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

    override fun solve1(input: List<LightDiagram>, onProgressUpdate: suspend (Progress) -> Unit): String {
        val sumPresses = input.sumOf { it.solveCount1() }
        return sumPresses.toString()
    }

    override fun solve2(input: List<LightDiagram>, onProgressUpdate: suspend (Progress) -> Unit): String {
        val sumPresses = input.sumOf { it.solveCount2() }
        return sumPresses.toString()
    }
}

data class LightDiagram(
    val targetLightsOn: Set<Int>,
    val size: Int,
    val buttonToggles: List<ButtonToggle>,
    val requiredJoltage: List<Int>
) {

    fun solveCount1(): Int {
        val nbButtons = buttonToggles.size
        val allCombinations = generateCombinations(nbButtons)
        for (i in 1..nbButtons) {
            val combinations = allCombinations[i]
                ?: throw IllegalArgumentException("All combinations must be provided")
            for (combination in combinations) {
                if (withIndices(combination)) {
                    return i
                }
            }
        }
        throw IllegalArgumentException("Impossible to solve")
    }
    fun solveCount2(): Int {
        TODO()
    }


    fun withIndices(listIndices: List<Int>): Boolean {
        val buttonsUsed = listIndices.map { index -> buttonToggles[index] }
        var indicators = emptySet<Int>()
        for (button in buttonsUsed) {
            indicators = button.toggle(indicators)
        }
        if (indicators.size != targetLightsOn.size) {
            return false
        }
        if (indicators.minus(targetLightsOn).isNotEmpty()) {
            return false
        }
        return true
    }
}

private var allCombinations = mutableMapOf<Combination, List<List<Int>>>()
private fun generateCombinations(n: Int): Map<Int, List<List<Int>>> {
    val combinations = mutableMapOf<Int, List<List<Int>>>()
    for (k in 1..n) {
        // take k parmi n
        val kParmiN = kParmiN(k, n)
        combinations[k] = kParmiN
    }
    return combinations
}

private data class Combination(val k: Int, val n: Int)

fun kParmiN(k: Int, n: Int): List<List<Int>> {
    if (allCombinations.contains(Combination(k, n))) {
        return allCombinations.getValue(Combination(k, n))
    }
    val combinations = mutableListOf<List<Int>>()
    fun backtrack(start: Int, currentCombination: MutableList<Int>) {
        if (currentCombination.size == k) {
            combinations.add(currentCombination.toList())
            return
        }
        for (i in start until n) {
            currentCombination.add(i)
            backtrack(i + 1, currentCombination)
            currentCombination.removeAt(currentCombination.size - 1)
        }
    }
    backtrack(0, mutableListOf())
    allCombinations[Combination(k, n)] = combinations
    return combinations
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
