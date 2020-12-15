package advent

import java.math.BigInteger
import java.util.*
import kotlin.math.pow

fun solve14(scanner: Scanner): String {
    with(scanner) {
        val maskAndMemories = mutableListOf<MaskAndMemory>()
        val regexMask = Regex("mask = (?<mask>[X01]+)")
        val regexMemory =
            Regex("mem\\[(?<memAddress>[0-9]+)] = (?<number>[0-9]+)")
        var currentList: MutableList<MemoryAction> =
            mutableListOf()
        while (hasNext()) {
            val line = nextLine()
            if (regexMask.matches(line)) {
                val mask = regexMask.matchEntire(line)!!.groups["mask"]!!.value
                currentList = mutableListOf()
                maskAndMemories.add(MaskAndMemory(mask, currentList))
                continue
            }
            val matchResult =
                regexMemory.matchEntire(line) ?: throw IllegalArgumentException(
                    "line not correct $line"
                )
            currentList.add(
                MemoryAction(
                    matchResult.groups["memAddress"]!!.value.toInt(),
                    matchResult.groups["number"]!!.value.toInt()
                )
            )
        }
        return sumPart2(valuesInMemory(maskAndMemories)).toString()
    }
}

fun valuesInMemory(
    maskAndMemory: List<MaskAndMemory>
): Map<BigInteger, Long> {
    val addressToValue = mutableMapOf<BigInteger, Long>()
    for (mAndMem in maskAndMemory) {
        val mask = maskToSparse(mAndMem.mask)
        for (m in mAndMem.memoryActions) {
            val addresses: List<BigInteger> = applyMask2(m.address, mask)
            for (a in addresses) {
                addressToValue[a] = m.value.toLong()
            }
        }
    }
    return addressToValue
}

fun applyMask2(address: Int, mask: Set<Pair<Int, Boolean>>): List<BigInteger> {
    val binaryString =
        StringBuilder(Integer.toBinaryString(address).padStart(36, '0'))
    // change all 1 with 1
    mask.filter { it.second }.forEach { binaryString[it.first] = '1' }
    val res = mutableListOf<BigInteger>()
    val floating = mask.filter { !it.second }.map { it.first }
    // now floating bits
    val switches: MutableList<Boolean> = MutableList(floating.size) { false }
    var i = 0L
    val maxI = 2.0.pow(floating.size).toLong()
    while (i < maxI) {
        incrementSwitchesCounter(i, switches)
        floating.forEachIndexed { index, floatingIndex ->
            binaryString[floatingIndex] =
                if (switches[index]) '1' else '0'
        }
        res.add(BigInteger(binaryString.toString(), 2))
        // increment switches
        i++
    }
    return res
}

fun incrementSwitchesCounter(i: Long, switches: MutableList<Boolean>) {
    val binaryString =
        Integer.toBinaryString(i.toInt()).padStart(switches.size, '0')
    for (index in binaryString.indices) {
        switches[index] = binaryString[index] == '1'
    }
}

fun maskToSparse(mask: String): Set<Pair<Int, Boolean>> =
    mask.foldIndexed(mutableSetOf()) { index, acc, c ->
        when (c) {
            '1' -> acc.add(Pair(index, true))
            'X' -> acc.add(Pair(index, false))
        }
        acc
    }

fun applyMask(value: Int, mask: Set<Pair<Int, Boolean>>): Long {
    val binaryString =
        StringBuilder(Integer.toBinaryString(value).padStart(36, '0'))
    for (m in mask) {
        binaryString[m.first] = if (m.second) '1' else '0'
    }
    return binaryString.toString().toLong(2)
}

fun part1(addressToValue: Map<Int, Long>): Long {
    return addressToValue.values.sum()
}

fun sumPart2(valuesInMemory: Map<BigInteger, Long>): Long {
    return valuesInMemory.values.sum()
}

data class MemoryAction(val address: Int, val value: Int)
data class MaskAndMemory(
    val mask: String,
    val memoryActions: List<MemoryAction>
)