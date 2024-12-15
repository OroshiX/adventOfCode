package advent.days

import advent.DayPuzzle
import advent.Position
import java.util.*
import kotlin.math.abs

class Day10 : DayPuzzle<HeightMap>() {
    override fun parse(scanner: Scanner): HeightMap {
        val result = mutableListOf<List<Int>>()
        val trailHeads = mutableListOf<Position>()
        var i = 0
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val numbers = line.mapIndexed { j, c ->
                c.digitToInt().also {
                    if (it == 0) trailHeads.add(Position(i, j))
                }
            }
            result.add(numbers)
            i++
        }
        return HeightMap(result, trailHeads)
    }

    override fun solve1(input: HeightMap): String {
        return input.trailHeads.sumOf { pos -> input.numberOfPaths(pos) }.toString()
    }

    override fun solve2(input: HeightMap): String {
        TODO()
    }
}

data class HeightMap(val map: List<List<Int>>, val trailHeads: List<Position>) {
    private val nbLines = map.size
    private val nbCols = map[0].size

    @Suppress("t")
    fun numberOfPaths(pos: Position): Int {
        val visited = mutableSetOf<Position>()
        val queue = ArrayDeque<Pair<Position, Int>>()
        queue.add(pos to 0)
        visited.add(pos)
        var count = 0
        while (queue.isNotEmpty()) {
            val (currentPos, currentVal) = queue.removeFirst()
            if (currentVal == 9) {
                count++
                continue
            }
            for (i in -1..1) {
                for (j in -1..1) {
                    if (i == 0 && j == 0 || abs(i) + abs(j) == 2) continue
                    val next = Position(currentPos.i + i, currentPos.j + j)
                    // If the next position is in bounds, has not been visited and has the right value
                    if (next.isInBounds(nbLines, nbCols) &&
                        next !in visited &&
                        map[next.i][next.j] == currentVal + 1
                    ) {
                        visited.add(next)
                        queue.add(next to currentVal + 1)
                    }
                }
            }
        }
        return count
    }

}
