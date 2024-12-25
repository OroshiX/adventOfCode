package advent.days

import advent.DayPuzzle
import java.util.*

class Day24 : DayPuzzle<CableNetwork>() {
    override fun parse(scanner: Scanner): CableNetwork {
        val inputs = mutableMapOf<String, Boolean>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (line.isBlank()) {
                break
            }
            val (id, value) = line.split(": ")
            inputs[id] = value == "1"
        }
        val cables = mutableListOf<Cable>()
        val wantedOutputsIds = mutableSetOf<String>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val (id1, operand, id2, _, result) = line.split(" ")
            cables.add(Cable(id1, id2, Operand.valueOf(operand), result))
            if (result.startsWith("z")) {
                wantedOutputsIds.add(result)
            }
        }
        return CableNetwork(cables, inputs, wantedOutputsIds)
    }

    override fun solve1(input: CableNetwork): String {
        val res = input.getZOutputs()
        val intRes = res.toBigInteger(2)

        return intRes.toString(10)
    }

    override fun solve2(input: CableNetwork): String {
        val triedSwaps = mutableSetOf<Set<Pair<String, String>>>()
        val allSwaps = generateCombinations(input.cables.map { it.idResult }.toList(), 2)
        val verite = tableVerite(input.wantedOutputsIds.size)
        loop@ for (swap in allSwaps) {
            if (triedSwaps.add(swap)) {
                val newNetwork = input.exchangeOutputs(swap)
                if ((swap.contains("z00" to "z01") || swap.contains("z01" to "z00"))
                    && (swap.contains("z02" to "z03") || swap.contains("z03" to "z02"))
                ) {
                    println("swap: $swap")
                    println(newNetwork)
                }
                for (truth in verite) {
                    val output = newNetwork.withInputs(truth.a, truth.b).getZOutputs()
                    if (output != truth.expected) {
                        // This is not the right swap
                        continue@loop
                    }
                }
                return printableSwap(swap)
            }
        }

        return "No swap found 🤷‍♂️"
    }
}

private fun printableSwap(swap: Set<Pair<String, String>>): String {
    return swap.flatMap { it.toList() }.sortedBy { it }.joinToString(",") { it }
}

private fun tableVerite(n: Int): List<TruthTableBinary> {
    assert(n % 2 == 0 && n >= 2)
    return listOf(
        TruthTableBinary("0".repeat(n), "0".repeat(n), "0".repeat(n)),
        TruthTableBinary("0".repeat(n), "1".repeat(n), "1".repeat(n)),
        TruthTableBinary("1".repeat(n), "0".repeat(n), "1".repeat(n)),
        TruthTableBinary("0" + "1".repeat(n - 1), "0" + "1".repeat(n - 1), "1".repeat(n - 1) + "0"),
        TruthTableBinary("01".repeat(n / 2), "01".repeat(n / 2), "10".repeat(n / 2)),
        TruthTableBinary("01".repeat(n / 2), "10".repeat(n / 2), "1".repeat(n)),
        TruthTableBinary("10".repeat(n / 2), "01".repeat(n / 2), "1".repeat(n)),
    )
}

private data class TruthTableBinary(val a: String, val b: String, val expected: String)

data class Cable(val id1: String, val id2: String, val operand: Operand, val idResult: String)
data class CableNetwork(val cables: List<Cable>, val inputs: Map<String, Boolean>, val wantedOutputsIds: Set<String>) {
    private val outputs = inputs.toMutableMap()

    private fun getNumber(id: String, inputs: Map<String, Boolean>): String =
        inputs.filterKeys { it.startsWith(id) }.asSequence().sortedByDescending { it.key }.map { it.value }
            .map { if (it) '1' else '0' }.joinToString("")

    private fun calculateExpectedAdditionResult(): Map<String, Boolean> {
        val x = getNumber("x", inputs).toBigInteger(2)
        val y = getNumber("y", inputs).toBigInteger(2)

        val result = x + y
        val resultString = result.toString(2).reversed()
        val resultMap = mutableMapOf<String, Boolean>()
        for (i in resultString.indices) {
            resultMap["z${i.toString().padStart(2, '0')}"] = resultString[i] == '1'
        }
        println("x: $x, y: $y, result: $result, binary: ${result.toString(2)}, resultMap: $resultMap")
        return resultMap
    }

    fun exchangeOutputs(swapped: Set<Pair<String, String>>): CableNetwork {
        // swap the results of the cables
        val newCables = cables.toMutableList()
        for (swap in swapped) {
            val cableAIndex = newCables.indexOfFirst { it.idResult == swap.first }
            val cableBIndex = newCables.indexOfFirst { it.idResult == swap.second }
            val cableA = newCables[cableAIndex]
            val cableB = newCables[cableBIndex]
            newCables[cableAIndex] = cableA.copy(idResult = cableB.idResult)
            newCables[cableBIndex] = cableB.copy(idResult = cableA.idResult)
        }
        return CableNetwork(newCables, inputs, wantedOutputsIds)
    }

    fun getZOutputs(): String {
        for (id in wantedOutputsIds) {
            calculateOutput(id)
        }
        return getNumber("z", outputs)
    }

    private fun calculateOutput(id: String): Boolean {
        return outputs.getOrPut(id) {
            val cable = cables.first { it.idResult == id }
            val a = outputs[cable.id1] ?: calculateOutput(cable.id1)
            val b = outputs[cable.id2] ?: calculateOutput(cable.id2)
            val result = cable.operand.apply(a, b)
            result
        }
    }

    override fun toString(): String {
        return cables.joinToString("\n") { "${it.id1} ${it.operand} ${it.id2} -> ${it.idResult}" }
    }

    fun withInputs(x: String, y: String): CableNetwork {
        val newInputs = mutableMapOf<String, Boolean>()
        val size = x.length
        for (i in x.indices) {
            newInputs["x${(size - i).toString().padStart(2, '0')}"] = x[i] == '1'
        }
        for (i in y.indices) {
            newInputs["y${(size - i).toString().padStart(2, '0')}"] = y[i] == '1'
        }
        return CableNetwork(cables, newInputs, wantedOutputsIds)

    }

}

val input = """
    

x00 XOR y00 -> z00
x00 AND y00 -> re0
x01 XOR y01 -> un1
re0 XOR un1 -> z01
x01 AND y01 -> re1
x02 XOR y02 -> un2
re1 XOR un2 -> z02
x02 AND y02 -> re2
x03 XOR y03 -> un3
re2 XOR un3 -> z03
x03 AND y03 -> re3
x04 XOR y04 -> un4
re3 XOR un4 -> z04
x04 AND y04 -> re4
x05 XOR y05 -> un5
re4 XOR un5 -> z05
x05 AND y05 -> re5
x06 XOR y06 -> un6
re5 XOR un6 -> z06
"""

enum class Operand {
    AND, OR, XOR;

    fun apply(a: Boolean, b: Boolean): Boolean {
        return when (this) {
            AND -> a && b
            OR -> a || b
            XOR -> a xor b
        }
    }
}

fun generateCombinations(
    list: List<String>,
    remainingPairs: Int,
    usedIndices: Set<Int> = emptySet(),
    currentPairs: List<Pair<String, String>> = emptyList()
): Set<Set<Pair<String, String>>> {
    if (remainingPairs == 0) {
        return setOf(currentPairs.toSet()) // Base case: all pairs are formed
    }

    val result = mutableSetOf<Set<Pair<String, String>>>()

    // Loop through all possible first elements of the pair
    for (i in list.indices) {
        if (i in usedIndices) continue // Skip already used indices

        // Loop through all possible second elements of the pair
        for (j in i + 1 until list.size) {
            if (j in usedIndices) continue // Skip already used indices

            // Form a new pair and recursively find combinations for remaining pairs
            val newUsedIndices = usedIndices + setOf(i, j)
            val newPairs = currentPairs + (list[i] to list[j])

            result += generateCombinations(
                list,
                remainingPairs - 1,
                newUsedIndices,
                newPairs
            )
        }
    }

    return result
}

fun main() {
    val allCables = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i")

    // Usage
    val swaps = generateCombinations(allCables, 4) // Generate 4 pairs

//    val allCombinations = generateCombinations(allCables, 8)

    println(swaps.joinToString("\n") { swap -> swap.joinToString(", ") { it.first + "<->" + it.second } })
}
