package advent

import java.util.*

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
        return part1(valuesInMemory(maskAndMemories)).toString()
    }
}

fun valuesInMemory(
    maskAndMemory: List<MaskAndMemory>
): Map<Int, Long> {
    val addressToValue = mutableMapOf<Int, Long>()
    for (mAndMem in maskAndMemory) {
        val mask = maskToSparse(mAndMem.mask)
        for (m in mAndMem.memoryActions) {
            addressToValue[m.address] = applyMask(m.value, mask)
        }
    }
    return addressToValue
}

fun maskToSparse(mask: String): Set<Pair<Int, Boolean>> =
    mask.foldIndexed(mutableSetOf()) { index, acc, c ->
        when (c) {
            '1' -> acc.add(Pair(index, true))
            '0' -> acc.add(Pair(index, false))
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

data class MemoryAction(val address: Int, val value: Int)
data class MaskAndMemory(
    val mask: String,
    val memoryActions: List<MemoryAction>
)