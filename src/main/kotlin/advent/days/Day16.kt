package advent.days

import advent.DayPuzzle
import advent.Direction
import advent.Position
import java.util.*
import kotlin.math.abs

class Day16 : DayPuzzle<Labyrinth>() {
    override fun parse(scanner: Scanner): Labyrinth {
        val map = mutableListOf<List<Element>>()
        while (scanner.hasNextLine()) {
            map.add(scanner.nextLine().map { c ->
                when (c) {
                    '#' -> Element.WALL
                    '.' -> Element.EMPTY
                    'S' -> Element.START
                    'E' -> Element.END
                    else -> throw IllegalArgumentException("Unknown element $c")
                }
            })
        }
        return Labyrinth(map)
    }

    override fun solve1(input: Labyrinth): String {
        return input.calculatePathValue().toString()
    }

    override fun solve2(input: Labyrinth): String {
        TODO()
    }
}

data class Labyrinth(val map: List<List<Element>>) {
    private val nbLines = map.size
    private val nbCols = map[0].size
    private val startingPoint = map.indexOfFirst { it.contains(Element.START) }
        .let { i ->
            val j = map[i].indexOfFirst { it == Element.START }
            Position(i, j)
        }

    private val endingPoint = map.indexOfFirst { it.contains(Element.END) }
        .let { i ->
            val j = map[i].indexOfFirst { it == Element.END }
            Position(i, j)
        }

    private val dijkstraResult = dijkstra()

    fun findShortestPath(): List<Position> {
        val (distances, previous) = dijkstraResult
        val path = mutableListOf<Position>()

        var current: Position? = endingPoint
        if (previous.containsKey(current) || current == startingPoint) {
            while (current != null) {
                path.add(0, current)
                current = previous[current]
            }
        }
        return path
    }

    fun calculatePathValue(): Int {
        val (distances, previous) = dijkstraResult
        return distances.getValue(endingPoint).first
    }

    /**
     * Dijkstra's algorithm for getting all the distances from the starting point to all the other points
     * and prev points to reconstruct the path
     */

    private fun dijkstra(): DijkstraResult {
        val distances = mutableMapOf<Position, Pair<Int, Direction>>()
        val previousPositions = mutableMapOf<Position, Position>()
        val queue = PriorityQueue<PositionAndPriority>()

        distances[startingPoint] = 0 to Direction.RIGHT
        queue.add(PositionAndPriority(startingPoint, 0))

        while (queue.isNotEmpty()) {
            val (current, _) = queue.poll()
            // for each neighbor of the current position

            for (direction in Direction.cross) {
                val neighbor = current + direction
                if (neighbor.isInBounds(nbLines, nbCols) &&
                    map[neighbor.i][neighbor.j] != Element.WALL
                ) {
                    val (dist, associatedDirection) = distances.getValue(current)
                    val alt = dist + scoreStep(current, associatedDirection, neighbor)
                    if (alt < (distances[neighbor]?.first ?: Int.MAX_VALUE)) {
                        previousPositions[neighbor] = current
                        distances[neighbor] = alt to direction
                        queue.add(PositionAndPriority(neighbor, alt))
                    }
                }
            }
        }
        return DijkstraResult(distances, previousPositions)
    }

    private fun scoreStep(start: Position, startDirection: Direction, end: Position): Int {
        val direction = end - start
        return when {
            start == end -> 0
            abs(start.i - end.i) + abs(start.j - end.j) != 1 -> throw IllegalArgumentException("Positions are not adjacent")
            direction == startDirection -> 1
            direction == startDirection.rotateLeft() || startDirection == direction.rotateLeft() -> 1001
            else -> 2001
        }
    }

    override fun toString(): String {
        return map.joinToString("\n") { row ->
            row.joinToString("") { element ->
                when (element) {
                    Element.WALL -> "#"
                    Element.EMPTY -> "."
                    Element.START -> "S"
                    Element.END -> "E"
                }
            }
        }
    }
}

private operator fun Position.minus(position: Position): Direction {
    if (i != position.i && j != position.j) {
        throw IllegalArgumentException("Positions are not aligned")
    }
    if (i == position.i && j == position.j) {
        throw IllegalArgumentException("Positions are the same")
    }
    return when {
        i < position.i -> Direction.UP
        i > position.i -> Direction.DOWN
        j < position.j -> Direction.LEFT
        else -> Direction.RIGHT
    }
}

private data class DijkstraResult(
    val distances: Map<Position, Pair<Int, Direction>>,
    val previousPosition: Map<Position, Position>
)

data class PositionAndPriority(val position: Position, val priority: Int) :
    Comparable<PositionAndPriority> {
    override fun compareTo(other: PositionAndPriority): Int {
        return priority.compareTo(other.priority)
    }
}

enum class Element {
    WALL, EMPTY, START, END
}

