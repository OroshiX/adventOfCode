package advent.days

import advent.DayPuzzle
import advent.Position
import java.util.*

class Day6 : DayPuzzle<Grid>() {
    override fun parse(scanner: Scanner): Grid {
        val terrain = mutableListOf<List<Terrain>>()
        var guard: Pair<Position, Direction>? = null
        var i = 0
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (line.isEmpty()) {
                break
            }
            val row = line.mapIndexed { j, c ->
                when (c) {
                    '.' -> Terrain.EMPTY
                    '#' -> Terrain.OBSTACLE
                    '^' -> {
                        guard = Position(i, j) to Direction.UP
                        Terrain.EMPTY
                    }

                    'v' -> {
                        guard = Position(i, j) to Direction.DOWN
                        Terrain.EMPTY
                    }

                    '<' -> {
                        guard = Position(i, j) to Direction.LEFT
                        Terrain.EMPTY
                    }

                    '>' -> {
                        guard = Position(i, j) to Direction.RIGHT
                        Terrain.EMPTY
                    }

                    else -> throw IllegalArgumentException("Invalid terrain")
                }
            }
            terrain.add(row)
            i++
        }
        return Grid(guard!!, terrain)
    }

    override fun solve1(input: Grid): String {
        input.moveGuard()
        return input.visited.map { it.first }.toSet().size.toString()
    }

    override fun solve2(input: Grid): String {
        input.moveGuard()
        var nbLoops = 0
        val obstaclesPositions = mutableSetOf<Position>()
        for (visited in input.visited) {
            // Get the next position of the guard
            val nextPosition = visited.move()
            val newGrid = input.newGridWithObstacleAt(nextPosition, obstaclesPositions) ?: continue

            newGrid.moveGuard()
            if (newGrid.isLoop) {
                nbLoops++
                if (nbLoops % 100 == 0) {
                    println("Loop $nbLoops detected: \n")
                    println(newGrid)
                    println()
                }
            }
        }
        return nbLoops.toString()
    }
}

private val Terrain.symbol: Char
    get() = when (this) {
        Terrain.EMPTY -> '.'
        Terrain.OBSTACLE -> '#'
    }
private val Direction.symbol: Char
    get() = when (this) {
        Direction.UP -> '^'
        Direction.DOWN -> 'v'
        Direction.LEFT -> '<'
        Direction.RIGHT -> '>'
        else -> throw IllegalArgumentException("Invalid direction")
    }

data class Grid(
    var guard: Pair<Position, Direction>,
    val terrain: List<List<Terrain>>,
    val visited: MutableSet<Pair<Position, Direction>> = mutableSetOf(),
    val newObstacle: Position? = null,
) {
    private val nbLines = terrain.size
    private val nbCols = terrain[0].size
    var isLoop = false

    private fun Position.isInBounds(): Boolean {
        return i in 0 until nbLines && j in 0 until nbCols
    }

    fun moveGuard() {
        var position: Position
        visited.add(guard)
        while (!isLoop) {
            position = guard.move()

            if (!position.isInBounds()) {
                break
            }

            guard = when (terrain[position.i][position.j]) {
                Terrain.EMPTY -> {
                    position to guard.second
                }

                Terrain.OBSTACLE -> {
                    guard.first to guard.second.next()
                }
            }
            isLoop = !visited.add(guard)
        }
        // End of the grid
    }

    fun newGridWithObstacleAt(
        newObstacle: Position,
        previousObstacles: MutableSet<Position>
    ): Grid? {
        val guardFirstPosition = visited.first()
        // Skip the first position of the guard
        if (newObstacle == guardFirstPosition.first || previousObstacles.contains(newObstacle)) {
            return null
        }
        val newGrid = Grid(
            terrain = terrain.mapIndexed { i, row ->
                row.mapIndexed { j, terrain ->
                    if (Position(i, j) == newObstacle) {
                        Terrain.OBSTACLE
                    } else {
                        terrain
                    }
                }
            },
            guard = guardFirstPosition,
            visited = mutableSetOf(),
            newObstacle = newObstacle
        )
        if (newGrid.terrain == terrain) {
            return null
        }
        previousObstacles.add(newObstacle)
        return newGrid
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (i in terrain.indices) {
            for (j in terrain[i].indices) {
                val position = Position(i, j)
                if (position == newObstacle) {
                    builder.append('O')
                } else if (position == guard.first) {
                    builder.append(guard.second.symbol)
                } else if (visited.map { it.first }.contains(position)) {
                    visited.filter { it.first == position }.map { it.second }.distinct().let {
                        if (it.none { d -> d == Direction.UP || d == Direction.DOWN }) {
                            builder.append("-")
                        } else if (it.none { d -> d == Direction.LEFT || d == Direction.RIGHT }) {
                            builder.append("|")
                        } else {
                            builder.append('+')
                        }
                    }
                } else {
                    builder.append(terrain[i][j].symbol)
                }
            }
            builder.append("\n")
        }
        return builder.toString()
    }
}

enum class Terrain {
    EMPTY, OBSTACLE
}

private fun Pair<Position, Direction>.move(): Position {
    return when (second) {
        Direction.UP -> Position(first.i - 1, first.j)
        Direction.DOWN -> Position(first.i + 1, first.j)
        Direction.LEFT -> Position(first.i, first.j - 1)
        Direction.RIGHT -> Position(first.i, first.j + 1)
        else -> throw IllegalArgumentException("Invalid direction")
    }
}

private fun Direction.next(): Direction {
    return when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
        else -> throw IllegalArgumentException("Invalid direction")
    }
}
