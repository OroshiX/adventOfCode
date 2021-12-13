package advent

import java.util.*
import kotlin.math.pow

fun solve8(scanner: Scanner): String {
    val inputOutput = mutableListOf<Pair<List<String>, List<String>>>()
    with(scanner) {
        var res = 0L
        while (hasNext()) {
            val (input, output) = nextLine().split("|")
            val inOut = input.trim().split(" ") to output.trim().split(" ")
            inputOutput += inOut
            res += part2(inOut)
        }
        return res.toString()
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

private fun part2(inOut: Pair<List<String>, List<String>>): Long {
    val association = Association(inOut.first)
    return association.read(inOut.second)
}

private data class Association(val posToChar: MutableMap<SegmentPosition, Set<Char>> = mutableMapOf<SegmentPosition, Set<Char>>()) {
    private val intToSegments = mapOf(
        0 to setOf(
            SegmentPosition.A,
            SegmentPosition.B,
            SegmentPosition.C,
            SegmentPosition.E,
            SegmentPosition.F,
            SegmentPosition.G
        ),
        1 to setOf(SegmentPosition.C, SegmentPosition.F),
        2 to setOf(
            SegmentPosition.A,
            SegmentPosition.C,
            SegmentPosition.D,
            SegmentPosition.E,
            SegmentPosition.G
        ),
        3 to setOf(
            SegmentPosition.A,
            SegmentPosition.C,
            SegmentPosition.D,
            SegmentPosition.F,
            SegmentPosition.G
        ),
        4 to setOf(SegmentPosition.B, SegmentPosition.C, SegmentPosition.D, SegmentPosition.F),
        5 to setOf(
            SegmentPosition.A,
            SegmentPosition.B,
            SegmentPosition.D,
            SegmentPosition.F,
            SegmentPosition.G
        ),
        6 to setOf(
            SegmentPosition.A,
            SegmentPosition.B,
            SegmentPosition.D,
            SegmentPosition.E,
            SegmentPosition.F,
            SegmentPosition.G
        ),
        7 to setOf(SegmentPosition.A, SegmentPosition.C, SegmentPosition.F),
        8 to setOf(
            SegmentPosition.A,
            SegmentPosition.B,
            SegmentPosition.C,
            SegmentPosition.D,
            SegmentPosition.E,
            SegmentPosition.F,
            SegmentPosition.G
        ),
        9 to setOf(
            SegmentPosition.A,
            SegmentPosition.B,
            SegmentPosition.C,
            SegmentPosition.D,
            SegmentPosition.F,
            SegmentPosition.G
        )
    )

    private val intToChars: Map<Int, Set<Char>>
        get() = intToSegments.mapValues {
            it.value.fold(mutableSetOf()) { acc, segmentPosition ->
                acc.union(posToChar.getValue(segmentPosition)).toMutableSet()
            }
        }

    constructor(input: List<String>) : this() {
        unique(
            twoLength = input.first { it.length == 2 },
            threeLength = input.first { it.length == 3 },
            fourLength = input.first { it.length == 4 },
            sevenLength = input.first { it.length == 7 })
        multiple(
            input = input
        )
    }

    private fun unique(
        twoLength: String,
        fourLength: String,
        threeLength: String,
        sevenLength: String
    ) {
        val oneCf = twoLength.toSet()
//        data[1] = oneCf.toSet()
        val four = fourLength.toSet()
//        data[4] = four.toSet()
        val seven = threeLength.toSet()
//        data[7] = seven.toSet()
        val eight = sevenLength.toSet()
//        data[8] = eight.toSet()
        posToChar[SegmentPosition.C] = oneCf.toSet()
        posToChar[SegmentPosition.F] = oneCf.toSet()
        posToChar[SegmentPosition.A] = seven.subtract(oneCf).toMutableSet()
        val bd = four.subtract(oneCf).toMutableSet()
        posToChar[SegmentPosition.B] = bd
        posToChar[SegmentPosition.D] = bd.toMutableSet()
        val eg = eight.subtract(four).subtract(seven).toMutableSet()
        posToChar[SegmentPosition.E] = eg
        posToChar[SegmentPosition.G] = eg.toMutableSet()
    }

    private fun multiple(input: List<String>) {
        val sixLength = input.filter { it.length == 6 }
        // d, e, or c missing
        // discriminate C and F positions
        val possibleC = posToChar.getValue(SegmentPosition.C)
        for (char in possibleC) {
            if (sixLength.count { it.contains(char) } == 2) {
                // c
                posToChar[SegmentPosition.C] = mutableSetOf(char)
            } else {
                // should be 3: f
                posToChar[SegmentPosition.F] = mutableSetOf(char)
            }
        }

        // discriminate B and D
        val possibleB = posToChar.getValue(SegmentPosition.B)
        for (char in possibleB) {
            if (input.count { it.contains(char) } == 6) {
                // b
                posToChar[SegmentPosition.B] = mutableSetOf(char)
            } else {
                // d
                posToChar[SegmentPosition.D] = mutableSetOf(char)
            }
        }

        // discriminate E and G
        val possibleE = posToChar.getValue(SegmentPosition.E)
        for (char in possibleE) {
            if (input.count { it.contains(char) } == 4) {
                // e
                posToChar[SegmentPosition.E] = mutableSetOf(char)
            } else {
                // g
                posToChar[SegmentPosition.G] = mutableSetOf(char)
            }
        }

    }


    fun read(output: List<String>): Long {
        var res = 0L
        for ((posDigit, i) in output.indices.reversed().withIndex()) {
            val d = readDigit(output[i].toSet())
            res += 10.0.pow(posDigit).toLong() * d
        }
        return res
    }

    private fun readDigit(digit: Set<Char>): Int {
        return intToChars.filterValues {
            it.subtract(digit).isEmpty() && digit.subtract(it).isEmpty()
        }.keys.first()
    }
}

private enum class SegmentPosition {
    A, B, C, D, E, F, G
}