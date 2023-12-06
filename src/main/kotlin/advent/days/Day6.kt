package advent.days

import advent.DayPuzzle
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.*

class Day6 : DayPuzzle<Races>() {
    override fun parse(scanner: Scanner): Races {
        val times = scanner.nextLine().substringAfter("Time:").trim().split(" +".toRegex()).map { it.toLong() }
        val distances = scanner.nextLine().substringAfter("Distance:").trim().split(" +".toRegex()).map { it.toLong() }
        return Races(times, distances)
    }

    override fun solve1(input: Races): String {
        var res = BigDecimal.ONE
        input.times.forEachIndexed { index, time ->
            val distance = input.distances[index]
            res *= numberPossibilities(time.toBigDecimal(), distance.toBigDecimal())
        }
        return res.toString()
    }

    override fun solve2(input: Races): String {
        val mergedTime = input.times.joinToString("") { it.toString() }.toLong()
        val mergedDistance = input.distances.joinToString("") { it.toString() }.toLong()
        return numberPossibilities(mergedTime.toBigDecimal(), mergedDistance.toBigDecimal()).toString()
    }

    private fun numberPossibilities(time: BigDecimal, bestDistance: BigDecimal): BigDecimal {
        val delta = time.pow(2) - BigDecimal(4) * bestDistance
        val deltaSq = delta.sqrt(MathContext.DECIMAL64)
        val x1 = (time + deltaSq) / BigDecimal(2)
        val x2 = (-time + deltaSq) / BigDecimal(-2)

        val minRoot = x1.min(x2)
        val maxRoot = x1.max(x2)
        val start =
            if (minRoot.rem(BigDecimal.ONE) == BigDecimal.ZERO) minRoot + BigDecimal.ONE
            else minRoot.setScale(0, RoundingMode.CEILING)
        val end =
            if (maxRoot.rem(BigDecimal.ONE) == BigDecimal.ZERO) maxRoot - BigDecimal.ONE
            else maxRoot.setScale(0, RoundingMode.FLOOR)
        return end - start + BigDecimal.ONE

    }

}

data class Races(val times: List<Long>, val distances: List<Long>) {
    init {
        assert(times.size == distances.size) {
            "bad input"
        }
    }
}