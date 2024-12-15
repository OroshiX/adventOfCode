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
        return input.visited.size.toString()
    }

    override fun solve2(input: Grid): String {
        TODO()
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
    val visited: MutableSet<Position> = mutableSetOf()
) {
    private val nbLines = terrain.size
    private val nbCols = terrain[0].size

    private fun Position.isInBounds(): Boolean {
        return i in 0 until nbLines && j in 0 until nbCols
    }

    fun moveGuard() {
        var position = guard.first
        visited.add(position)
        while (true) {
            position = guard.move()

            if (!position.isInBounds()) {
                break
            }

            guard = when (terrain[position.i][position.j]) {
                Terrain.EMPTY -> {
                    visited.add(position)
                    position to guard.second
                }

                Terrain.OBSTACLE -> {
                    guard.first to guard.second.next()
                }
            }
        }
        // End of the grid
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (i in terrain.indices) {
            for (j in terrain[i].indices) {
                val position = Position(i, j)
                if (position == guard.first) {
                    builder.append(guard.second.symbol)
                } else if (visited.contains(position)) {
                    builder.append("x")
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
