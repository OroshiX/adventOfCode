package advent

import java.util.*

fun solve6(scanner: Scanner): String {
    with(scanner) {
        val list = nextLine().split(",").map { it.toInt() }
        val fishes = Fishes(list)

        fishes.steps(256)

        return fishes.size.toString()
    }
}

private data class Fishes(val fishToNumber: MutableMap<Int, Long> = mutableMapOf()) {

    constructor(list: List<Int>) : this() {
        for (f in list) {
            fishToNumber[f] = fishToNumber.getOrDefault(f, 0) + 1
        }
    }

    constructor(line: String) : this(line.split(",").map { it.toInt() })

    private fun step() {
        val prec = fishToNumber[0] ?: 0
        for (i in 0..8) {
            fishToNumber[i] = fishToNumber[i + 1] ?: 0
        }
        fishToNumber[6] = fishToNumber.getOrDefault(6, 0) + prec
        fishToNumber[8] = prec
    }

    private fun step7() {
        val seven = fishToNumber.remove(7) ?: 0
        val eight = fishToNumber.remove(8) ?: 0
        for (i in 6 downTo 0) {
            fishToNumber[i + 2] =
                fishToNumber.getOrDefault(i + 2, 0) + fishToNumber.getOrDefault(i, 0)
        }
        fishToNumber[0] = fishToNumber.getOrDefault(0, 0) + seven
        fishToNumber[1] = fishToNumber.getOrDefault(1, 0) + eight
    }

    fun steps(n: Int) {
        val times = n / 7
        val turns = n % 7
        for (i in 0 until times) {
            step7()
        }
        for (i in 0 until turns) {
            step()
        }
    }

    val size: Long
        get() = fishToNumber.values.sum()
}

private fun Map<Int, Long>.trim(): Map<Int, Long> {
    return this.filterValues { it != 0L }
}

private fun assertion(line: String, firstLine: String) {
    val (_, n, _, data) = line.split(Regex(" +"))
    val mapEnd = Fishes(data).fishToNumber
    val fishes = Fishes(firstLine)
    fishes.steps(n.toInt())
    if (fishes.fishToNumber.trim() != mapEnd) {
        System.err.println("For day $n, fishes not equal:\n\t(actual) ${fishes.fishToNumber.trim()} != \n\t(expected) $mapEnd")
    }
}

fun main() {
    val test = """
        After  1 day:  2,3,2,0,1
        After  2 days: 1,2,1,6,0,8
        After  3 days: 0,1,0,5,6,7,8
        After  4 days: 6,0,6,4,5,6,7,8,8
        After  5 days: 5,6,5,3,4,5,6,7,7,8
        After  6 days: 4,5,4,2,3,4,5,6,6,7
        After  7 days: 3,4,3,1,2,3,4,5,5,6
        After  8 days: 2,3,2,0,1,2,3,4,4,5
        After  9 days: 1,2,1,6,0,1,2,3,3,4,8
        After 10 days: 0,1,0,5,6,0,1,2,2,3,7,8
        After 11 days: 6,0,6,4,5,6,0,1,1,2,6,7,8,8,8
        After 12 days: 5,6,5,3,4,5,6,0,0,1,5,6,7,7,7,8,8
        After 13 days: 4,5,4,2,3,4,5,6,6,0,4,5,6,6,6,7,7,8,8
        After 14 days: 3,4,3,1,2,3,4,5,5,6,3,4,5,5,5,6,6,7,7,8
        After 15 days: 2,3,2,0,1,2,3,4,4,5,2,3,4,4,4,5,5,6,6,7
        After 16 days: 1,2,1,6,0,1,2,3,3,4,1,2,3,3,3,4,4,5,5,6,8
        After 17 days: 0,1,0,5,6,0,1,2,2,3,0,1,2,2,2,3,3,4,4,5,7,8
        After 18 days: 6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8
    """.trimIndent().split("\n")
    val initState = "3,4,3,1,2"
    for (t in test) {
        assertion(t, initState)
    }
}