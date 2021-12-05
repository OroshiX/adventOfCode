package advent

import java.util.*
import kotlin.math.max
import kotlin.math.min

fun solve5(scanner: Scanner): String {
    with(scanner) {
        val lines = mutableListOf<Line>()
        while (hasNext()) {
            lines.add(Line(nextLine()))
        }
        val grid = Grid(lines)
        return grid.countOverlappingPoints.toString()
    }
}

private data class Grid(val lines: List<Line>) {
    val coverageMap: Map<Point, Int>
        get() {
            val res = mutableMapOf<Point, Int>()
            lines.forEach {
                val covered = it.coveredPoints
                for (cov in covered) {
                    res[cov] = res.getOrDefault(cov, 0) + 1
                }
            }
            return res
        }
    val countOverlappingPoints: Int
        get() = coverageMap.filterValues { it >= 2 }.count()
}

private data class Point(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x,$y)"
    }
}

private class Line(input: String) {
    val point1: Point
    val point2: Point

    init {
        val (x, y, xx, yy) = input.split(" -> ", ",").map { it.toInt() }
        point1 = Point(x, y)
        point2 = Point(xx, yy)
    }

    val maxCoordX: Int
        get() = max(point1.x, point2.x)
    val maxCoordY: Int
        get() = max(point1.y, point2.y)
    private val horizontal: Boolean
        get() = point1.y == point2.y
    private val vertical: Boolean
        get() = point1.x == point2.x
    val coveredPoints: List<Point>
        get() {
            val res = mutableListOf<Point>()
            if (vertical) {
                for (y in min(point1.y, point2.y)..maxCoordY) {
                    res += Point(point1.x, y)
                }
            } else if (horizontal) {
                for (x in min(point1.x, point2.x)..maxCoordX) {
                    res += Point(x, point1.y)
                }
            } else {
                // diagonal
                val progressionX: Int = if (point1.x < point2.x) 1 else -1
                val progressionY: Int = if (point1.y < point2.y) 1 else -1
                var current = point1.copy()
                var i = 0
                while (current != point2) {
                    res += current
                    i++
                    current = Point(point1.x + progressionX * i, point1.y + progressionY * i)
                }
                res+= current
            }
            return res
        }

    override fun toString(): String {
        return "$point1 -> $point2: $coveredPoints"
    }
}