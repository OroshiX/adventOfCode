package advent.days

import advent.DayPuzzle
import advent.Position
import java.util.*

class Day12 : DayPuzzle<GridAreas>() {
    override fun parse(scanner: Scanner): GridAreas {
        val grid = mutableListOf<List<Char>>()
        while (scanner.hasNextLine()) {
            grid.add(scanner.nextLine().toList())
        }
        return GridAreas(grid)
    }

    override fun solve1(input: GridAreas): String {
        var sum = 0
        input.areas.keys.forEach { area ->
            val areaSize = input.getAreaSize(area)
            val perimeter = input.getPerimeter(area)
            sum += areaSize * perimeter
            println("Area $area: size=$areaSize, perimeter=$perimeter")
        }
        return sum.toString()
    }

    override fun solve2(input: GridAreas): String {
        TODO()
    }
}

data class GridAreas(val grid: List<List<Char>>) {
    val areas: Map<Int, Set<Position>>

    init {
        val areas = mutableMapOf<Char, MutableSet<Position>>()
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val area = grid[i][j]
                areas.getOrPut(area) { mutableSetOf() }.apply {
                    add(Position(i, j))
                }
            }
        }
        this.areas = areas.separateAreas()
    }

    private fun Map<Char, Set<Position>>.separateAreas(): Map<Int, Set<Position>> {
        val newAreas = mutableMapOf<Int, Set<Position>>()
        var id = 0
        values.forEach { area ->
            id = separateArea(id, area, grid.size, grid[0].size, newAreas)
        }
        return newAreas
    }

    fun getAreaSize(area: Int): Int {
        return areas[area]?.size ?: 0
    }

    fun getPerimeter(area: Int): Int {
        return areas[area]?.getPerimeter(grid.size, grid[0].size) ?: 0
    }

    private fun Set<Position>.getPerimeter(nbLines: Int, nbCols: Int): Int {
        var perimeter = 0
        for (pos in this) {
            for (direction in directions) {
                val newPos = pos + direction
                when {
                    newPos.isInBounds(nbLines, nbCols).not() -> perimeter++
                    contains(newPos).not() -> perimeter++
                }
            }
        }
        return perimeter
    }

}

private operator fun Position.plus(direction: Direction): Position {
    return Position(i + direction.di, j + direction.dj)
}

private val directions = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)

private fun separateArea(
    id: Int,
    area: Set<Position>,
    nbLines: Int,
    nbCols: Int,
    newAreas: MutableMap<Int, Set<Position>>
): Int {
    var currentAreaId = id
    val positionsLeft = area.toMutableSet()

    while (positionsLeft.isNotEmpty()) {
        val visited = mutableSetOf<Position>()
        val newArea = mutableSetOf<Position>()
        val queue = ArrayDeque<Position>()
        queue.add(positionsLeft.first())
        visited.add(positionsLeft.first())
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            newArea.add(current)
            positionsLeft.remove(current)
            for (direction in directions) {
                val newPos = current + direction
                if (newPos.isInBounds(nbLines, nbCols) && newPos !in visited && positionsLeft.contains(newPos)) {
                    queue.add(newPos)
                }
                visited.add(newPos)
            }
        }
        newAreas[currentAreaId] = newArea
        currentAreaId++
    }
    return currentAreaId
}
