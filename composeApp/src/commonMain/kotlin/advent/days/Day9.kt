package advent.days

import advent.DayPuzzle
import advent.service.Progress
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Day9 : DayPuzzle<List<RedTile>>() {
    override fun parse(scanner: Scanner): List<RedTile> {
        val redTiles = mutableListOf<RedTile>()
        while (scanner.hasNext()) {
            val line = scanner.next()
            val (x, y) = line.split(",").map { it.toLong() }
            redTiles.add(RedTile(x, y))
        }
        return redTiles
    }

    override fun solve1(input: List<RedTile>, onProgressUpdate: suspend (Progress) -> Unit): String {
        var areaMax = 0L
        for (u in input.indices) {
            for (v in u + 1 until input.size) {
                val tile1 = input[u]
                val tile2 = input[v]
                val xDistance = max(tile2.x, tile1.x) - min(tile1.x, tile2.x) + 1
                val yDistance = max(tile2.y, tile1.y) - min(tile1.y, tile2.y) + 1
                val area = xDistance * yDistance
                if (area > areaMax) {
                    areaMax = area
                }
            }
        }
        return areaMax.toString()
    }

    override fun solve2(input: List<RedTile>, onProgressUpdate: suspend (Progress) -> Unit): String {
        TODO()
    }
}

data class RedTile(val x: Long, val y: Long)
