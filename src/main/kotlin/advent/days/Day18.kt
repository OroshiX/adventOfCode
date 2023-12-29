package advent.days

import advent.DayPuzzle
import advent.Position
import advent.terminal
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import com.github.ajalt.mordant.rendering.TextStyle
import org.jetbrains.annotations.VisibleForTesting
import java.util.*

class Day18 : DayPuzzle<Polygon>() {
    private val regex = """([UDLR]) ([0-9]+) \((#[0-9a-f]{6})\)""".toRegex()
    override fun parse(scanner: Scanner): Polygon {
        var currentPoint = Position(0, 0) to rgb("#000000")
        val points = mutableListOf<Pair<Position, TextStyle>>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            regex.matchEntire(line)?.let {
                val (direction, distance, color) = it.destructured
                terminal.println((rgb(color) on TextColors.black)("$direction $distance"))
                currentPoint = currentPoint.first.move(direction, distance.toInt()) to rgb(color)
                points += currentPoint
            } ?: throw IllegalArgumentException("Invalid line $line")
        }
        return Polygon(*points.toTypedArray())
    }

    override fun solve1(input: Polygon): String {
        terminal.println(input.printable())
        return input.getArea().toString()
    }

    override fun solve2(input: Polygon): String {
        TODO("Not yet implemented")
    }

}

private fun Position.move(direction: String, distance: Int): Position {
    return when (direction) {
        "U" -> Position(i - distance, j)
        "D" -> Position(i + distance, j)
        "L" -> Position(i, j - distance)
        "R" -> Position(i, j + distance)
        else -> throw IllegalArgumentException("Unknown direction $direction")
    }
}

private fun recenter(points: List<Pair<Position, TextStyle>>): List<Pair<Position, TextStyle>> {
    val minI = points.minOf { it.first.i }
    val minJ = points.minOf { it.first.j }
    return points.map { (position, color) ->
        Position(position.i - minI, position.j - minJ) to color
    }
}

data class Polygon(val points: List<Pair<Position, TextStyle>>) {
    private val minI = points.minOf { it.first.i }
    private val maxI = points.maxOf { it.first.i }
    private val minJ = points.minOf { it.first.j }
    private val maxJ = points.maxOf { it.first.j }

    constructor(vararg points: Pair<Position, TextStyle>) : this(recenter(points.toList()))

    fun printable(): String {
        val minI = points.minOf { it.first.i }
        val maxI = points.maxOf { it.first.i }
        val minJ = points.minOf { it.first.j }
        val maxJ = points.maxOf { it.first.j }
        val grid = Array(maxI - minI + 1) { Array(maxJ - minJ + 1) { "." } }
        var (previous, _) = points.last()
        points.forEach { (position, color) ->
            grid[position.i - minI][position.j - minJ] = color("+")
            if (position.i == previous.i) {
                val start = minOf(position.j, previous.j)
                val end = maxOf(position.j, previous.j)
                for (j in start + 1 until end) {
                    grid[position.i - minI][j - minJ] = color("-")
                }
            } else if (position.j == previous.j) {
                val start = minOf(position.i, previous.i)
                val end = maxOf(position.i, previous.i)
                for (i in start + 1 until end) {
                    grid[i - minI][position.j - minJ] = color("|")
                }
            }
            previous = position
        }
        return grid.joinToString("\n") { it.joinToString("") }
    }

    @VisibleForTesting
    fun isOnPerimeter(i: Int, j: Int): Boolean {
        // Check that the point is not on either of the segments in `points`
        points.forEachIndexed { k, (p, _) ->
            val next = points[(k + 1) % points.size].first
            if (p.i == next.i) {
                if (p.i == i && minOf(p.j, next.j) <= j && j <= maxOf(p.j, next.j)) {
                    return true
                }
            } else if (p.j == next.j) {
                if (p.j == j && minOf(p.i, next.i) <= i && i <= maxOf(p.i, next.i)) {
                    return true
                }
            }
        }
        return false
    }

    @VisibleForTesting
    fun isInside(i: Float, j: Float): Boolean {
        var inside = false
        // Raycast from the point to the right
        points.forEachIndexed { k, (p, _) ->
            val next = points[(k + 1) % points.size].first
            if (p.j < j && next.j >= j || next.j < j && p.j >= j) {
                if (p.i + (j - p.j) / (next.j - p.j) * (next.i - p.i) < i) {
                    inside = !inside
                }
            }
        }
        return inside
    }

    fun getPointsInsidePolygon(): List<Position> {
        val pointsInside = mutableListOf<Position>()
        for (i in minI..maxI) {
            for (j in minJ..maxJ) {
                if (isOnPerimeter(i, j)) {
                    pointsInside.add(Position(i, j))
                    continue
                }
                if (isInside(i + 0.5f, j + 0.5f)) {
                    pointsInside.add(Position(i, j))
                }
            }
        }
        return pointsInside
    }

    fun getArea(): Int {
        val pointsInside = getPointsInsidePolygon()
        return pointsInside.size
    }
}
