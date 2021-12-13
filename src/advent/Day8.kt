package advent

import java.util.*

fun solve8(scanner: Scanner): String {
    val inputOutput = mutableListOf<Pair<List<String>, List<String>>>()
    with(scanner) {
        while (hasNext()) {
            val (input, output) = nextLine().split("|")
            inputOutput += input.trim().split(" ") to output.trim().split(" ")
        }
        return part1(inputOutput).toString()
    }
}

private fun part1(inputOutput: List<Pair<List<String>, List<String>>>): Int {
    return inputOutput.fold(0) { acc, pair ->
        acc + pair.second.count {
            when (it.length) {
                2, 4, 3, 7 -> true
                else -> false
            }
        }
    }
}

