package advent.days

import advent.DayPuzzle
import java.util.*

class Day5 : DayPuzzle<Almanac>() {
    override fun parse(scanner: Scanner): Almanac {
        val seeds = scanner.nextLine().substringAfter("seeds: ").split(" ").map { it.toLong() }
        scanner.nextLine()
        return Almanac(
            seeds,
            parseMap(scanner),
            parseMap(scanner),
            parseMap(scanner),
            parseMap(scanner),
            parseMap(scanner),
            parseMap(scanner),
            parseMap(scanner),
        )
    }

    private fun parseMap(scanner: Scanner): List<SourceDestination> {
        scanner.nextLine()
        val map = mutableListOf<SourceDestination>()
        var line = scanner.nextLine()
        while (line.isNotEmpty()) {
            val (destination, source, length) = line.split(" ").map { it.toLong() }
            map += SourceDestination(source, destination, length)
            line = if (scanner.hasNextLine()) {
                scanner.nextLine()
            } else {
                ""
            }
        }
        return map
    }

    override fun solve1(input: Almanac): String {
        val locations = mutableListOf<Long>()
        for (seed in input.seedsToBePlanted) {
            locations += input.function(seed)
        }
        return locations.min().toString()
    }

    override fun solve2(input: Almanac): String {
        val locations = mutableListOf<Long>()
        for (i in 0..input.seedsToBePlanted.size - 2 step 2) {
            val start = input.seedsToBePlanted[i]
            val length = input.seedsToBePlanted[i + 1]
            var min = Long.MAX_VALUE
            for (j in start..<start + length) {
                val current = input.function(j)
                if (current < min) min = current
            }
            locations += min
        }
        return locations.min().toString()
    }

}

data class Almanac(
    val seedsToBePlanted: List<Long>,
    val seedSoil: List<SourceDestination>,
    val soilFertilizer: List<SourceDestination>,
    val fertilizerWater: List<SourceDestination>,
    val waterLight: List<SourceDestination>,
    val lightTemperature: List<SourceDestination>,
    val temperatureHumidity: List<SourceDestination>,
    val humidityLocation: List<SourceDestination>,
) {
    private val seedSoilOrdered: List<SourceDestination> = seedSoil.sorted()
    private val soilFertilizerOrdered: List<SourceDestination> = soilFertilizer.sorted()
    private val fertilizerWaterOrdered: List<SourceDestination> = fertilizerWater.sorted()
    private val waterLightOrdered: List<SourceDestination> = waterLight.sorted()
    private val lightTemperatureOrdered: List<SourceDestination> = lightTemperature.sorted()
    private val temperatureHumidityOrdered: List<SourceDestination> = temperatureHumidity.sorted()
    private val humidityLocationOrdered: List<SourceDestination> = humidityLocation.sorted()

    private fun next(input: Long, transformOrdered: List<SourceDestination>): Long {
        for (sourceDestination in transformOrdered) {
            if (input < sourceDestination.source) return input
            if (sourceDestination.inRange(input)) return sourceDestination.transform(input)
        }
        return input
    }

    fun function(input: Long): Long {
        var n = next(input, seedSoilOrdered)
        n = next(n, soilFertilizerOrdered)
        n = next(n, fertilizerWaterOrdered)
        n = next(n, waterLightOrdered)
        n = next(n, lightTemperatureOrdered)
        n = next(n, temperatureHumidityOrdered)
        n = next(n, humidityLocationOrdered)
        return n
    }
}

data class SourceDestination(val source: Long, val destination: Long, val length: Long) :
    Comparable<SourceDestination> {
    fun inRange(input: Long): Boolean {
        return input in (source..<source + length)
    }

    override fun compareTo(other: SourceDestination): Int = source.compareTo(other.source)
    fun transform(input: Long): Long {
        return destination + (input - source)
    }
}