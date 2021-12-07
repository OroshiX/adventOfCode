package advent

import java.util.*
import kotlin.math.abs

fun solve7(scanner: Scanner): String {
    with(scanner) {
        val crabs = nextLine().split(",").map { it.toInt() }
        return shortestPosition(crabs).toString()
    }
}

fun shortestPosition(crabs: List<Int>): Int {
    val sorted = crabs.sorted()
    val middle = sorted.size / 2
    val median =
        if (sorted.size % 2 == 0) (sorted[middle] + sorted[middle + 1]) / 2 else sorted[middle]
    val fuelMedian = totalFuel(crabs, median)
    shouldBeLess(sorted, median)?.let { return it }
    shouldBeMore(sorted, median)?.let { return it }
    println("[median] fuel consumed at $median: $fuelMedian")
    return fuelMedian
}

fun shouldBeLess(crabs: List<Int>, than: Int): Int? {
    var less = than
    var fuel = totalFuel(crabs, than)

    while (less >= crabs.first()) {
        less--
        val currentFuel = totalFuel(crabs, less)
        if (currentFuel > fuel) {
            println("[less] fuel consumed at ${less + 1}: $fuel")
            return if (less + 1 == than) null else fuel
        }
        fuel = currentFuel
    }
    return fuel
}

fun shouldBeMore(crabs: List<Int>, than: Int): Int? {
    var more = than
    var fuel = totalFuel(crabs, than)
    while (more <= crabs.last()) {
        more++
        val currentFuel = totalFuel(crabs, more)
        if (currentFuel > fuel) {
            println("[more] fuel consumed at ${more - 1}: $fuel")
            return if (more - 1 == than) null else fuel
        }
        fuel = currentFuel
    }
    return fuel
}

fun totalFuel(crabs: List<Int>, position: Int): Int {
    return crabs.fold(0) { acc, i -> acc + abs(i - position) }
}
