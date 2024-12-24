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
        val zOutputs = input.getZOutputs()
        val res = zOutputs.map { if (it) '1' else '0' }.joinToString("")
        val intRes = res.toBigInteger(2)

        return intRes.toString(10)
    }

    override fun solve2(input: CableNetwork): String {
        TODO()
    }
}

data class Cable(val id1: String, val id2: String, val operand: Operand, val idResult: String)
data class CableNetwork(val cables: List<Cable>, val inputs: Map<String, Boolean>, val wantedOutputsIds: Set<String>) {
    private val outputs = inputs.toMutableMap()

    fun getZOutputs(): List<Boolean> {
        for (id in wantedOutputsIds) {
            calculateOutput(id)
        }
        return outputs.filterKeys { it.startsWith("z") }.toList().sortedByDescending { it.first }.map { it.second }
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
}

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
