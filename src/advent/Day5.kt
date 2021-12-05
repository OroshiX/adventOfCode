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
    val sizeX: Int
        get() = lines.maxOf { it.maxCoordX }
    val sizeY: Int
        get() = lines.maxOf { it.maxCoordY }
    val coverageMap: Map<Point, Int>
        get() {
            val res = mutableMapOf<Point, Int>()
            lines.forEach {
                val covered = it.coveredPointsAxis
                for (cov in covered) {
                    res[cov] = res.getOrDefault(cov, 0) + 1
                }
            }
            return res
        }
    val countOverlappingPoints: Int
        get() = coverageMap.filterValues { it >= 2 }.count()
}

private data class Point(val x: Int, val y: Int)

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
    val coveredPointsAxis: List<Point>
        get() {
            val res = mutableListOf<Point>()
            if (vertical) {
                for (y in min(point1.y, point2.y)..maxCoordY) {
                    res += Point(point1.x, y)
                }
            }
            if (horizontal) {
                for (x in min(point1.x, point2.x)..maxCoordX) {
                    res += Point(x, point1.y)
                }
            }
            return res
        }
}