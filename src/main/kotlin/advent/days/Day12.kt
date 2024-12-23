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
        var sum = 0
        input.areas.keys.forEach { area ->
            val areaSize = input.getAreaSize(area)
            val sides = input.getNbOfSides(area)
            sum += areaSize * sides
            println("Area $area: size=$areaSize, sides=$sides")
        }
        return sum.toString()
    }
}

data class GridAreas(val grid: List<List<Char>>) {
    private val nbLines = grid.size
    private val nbCols = grid[0].size
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
        return areas[area]?.getPerimeter() ?: 0
    }

    fun getNbOfSides(area: Int): Int {
        return areas[area]?.getNbOfSides() ?: 0
    }

    private fun Set<Position>.getPerimeter(): Int {
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

    private fun Set<Position>.getNbOfSides(): Int {
        var nbOfSides = 0
        // if 2 neighbouring positions are at the top edge of the area, then it forms a side, along with all the positions in between
        //  and each side is counted only once of the whole polygon
        val visited = mutableSetOf<Pair<Position, Direction>>()
        for (pos in this) {
            for (direction in directions) {
                if (visited.contains(pos to direction)) continue

                val newPos = pos + direction
                when {
                    newPos.isInBounds(nbLines, nbCols).not() || contains(newPos).not() -> {
                        visitBeforeAndAfter(pos, direction, visited)
                        nbOfSides++
                    }
                }
            }
        }
        return nbOfSides
    }

    private fun Set<Position>.visitBeforeAndAfter(
        pos: Position,
        direction: Direction,
        visited: MutableSet<Pair<Position, Direction>>
    ) {
        val directionBefore = direction.rotateLeft()
        val directionAfter = direction.rotateRight()
        var newPos = pos + directionBefore
        while (newPos.isInBounds(nbLines, nbCols) && contains(newPos)) {
            visited.add(newPos to direction)
            val neighbour = newPos + direction
            when {
                // Is at the edge of the area
                neighbour.isInBounds(nbLines, nbCols).not() || contains(neighbour).not() -> {
                    newPos += directionBefore
                }

                else -> break
            }
        }
        newPos = pos + directionAfter
        while (newPos.isInBounds(nbLines, nbCols) && contains(newPos)) {
            visited.add(newPos to direction)
            val neighbour = newPos + direction
            when {
                // Is at the edge of the area
                neighbour.isInBounds(nbLines, nbCols).not() || contains(neighbour).not() -> {
                    newPos += directionAfter
                }

                else -> break
            }
        }
    }

}

private fun Direction.rotateRight(): Direction {
    return when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
        else -> throw IllegalArgumentException("Invalid direction")
    }
}

private fun Direction.rotateLeft(): Direction {
    return when (this) {
        Direction.UP -> Direction.LEFT
        Direction.LEFT -> Direction.DOWN
        Direction.DOWN -> Direction.RIGHT
        Direction.RIGHT -> Direction.UP
        else -> throw IllegalArgumentException("Invalid direction")
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
