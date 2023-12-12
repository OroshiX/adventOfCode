package advent.days

import advent.DayPuzzle
import advent.Position
import java.util.*

class Day10 : DayPuzzle<MatrixGraph>() {
    override fun parse(scanner: Scanner): MatrixGraph {
        var startPos = Position(0, 0)
        val pipes = mutableListOf<MutableList<PipeType>>()
        var row = 0
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            pipes += line.mapIndexed { j, c ->
                if (c == 'S') startPos = Position(row, j)
                PipeType.fromChar(c)
            }.toMutableList()
            row++
        }
        val startCellDirections = mutableListOf<Direction>()
        for (direction in Direction.entries) {
            val i = startPos.i + direction.i
            val j = startPos.j + direction.j
            if (i < 0 || i >= row || j < 0 || j >= pipes.first().size) continue
            val cell = pipes[i][j]
            if (cell.hasDirection(direction.opposite)) {
                startCellDirections += direction
            }
        }
        if (startCellDirections.size != 2) throw IllegalStateException("Start cell has ${startCellDirections.size} directions")
        pipes[startPos.i][startPos.j] = PipeType.fromDirections(startCellDirections)
        return MatrixGraph(parseTransformToMatrix(pipes), startPos.toIndex(pipes.first().size))
    }

    private fun parseTransformToMatrix(pipes: List<List<PipeType>>): List<List<Boolean>> {
        val maxCol = pipes.first().size
        val matrix = MutableList(pipes.size * maxCol) { MutableList(pipes.size * maxCol) { false } }
        for (kEntry in 0 until pipes.size * maxCol) {
            val entry = kEntry.toPosition(maxCol)
            val pipe = pipes[entry.i][entry.j]
            pipe.directions.forEach { direction ->
                val i = entry.i + direction.i
                val j = entry.j + direction.j
                if (i < 0 || i >= pipes.size || j < 0 || j >= maxCol) return@forEach
                matrix[kEntry][Position(i, j).toIndex(maxCol)] = true
            }
        }
        return matrix
    }

    override fun solve1(input: MatrixGraph): String {
        return input.noRec(input.start, mutableSetOf()).toString()
    }


    override fun solve2(input: MatrixGraph): String {
        TODO("Not yet implemented")
    }
}

data class MatrixGraph(val matrix: List<List<Boolean>>, val start: Int) {
    private fun biDirectionalConnections(index: Int): List<Int> {
        return matrix[index].mapIndexed { k, b -> if (b && matrix[k][index]) k else null }
            .filterNotNull()
    }

    fun noRec(position: Int, alreadyVisited: MutableSet<Int>): Int {
        var result = 0
        val queue = mutableListOf(position to 0)
        while (queue.isNotEmpty()) {
            val (index, distance) = queue.removeAt(0)
            if (alreadyVisited.contains(index)) continue
            val nextConnections = biDirectionalConnections(index)
            queue.addAll(nextConnections.map { it to distance + 1 })
            alreadyVisited.add(index)
            result = maxOf(result, distance)
        }
        return result
    }
}

private fun Int.toPosition(colSize: Int): Position {
    return Position(this / colSize, this % colSize)
}

private fun Position.toIndex(colSize: Int): Int {
    return i * colSize + j
}

enum class PipeType(val representation: Char, val directions: List<Direction>) {
    HORIZONTAL('-', listOf(Direction.EAST, Direction.WEST)),
    VERTICAL('|', listOf(Direction.NORTH, Direction.SOUTH)),
    NORTH_EAST('L', listOf(Direction.NORTH, Direction.EAST)),
    NORTH_WEST('J', listOf(Direction.NORTH, Direction.WEST)),
    SOUTH_EAST('F', listOf(Direction.SOUTH, Direction.EAST)),
    SOUTH_WEST('7', listOf(Direction.WEST, Direction.SOUTH)), GROUND('.', emptyList());

    fun hasDirection(direction: Direction): Boolean {
        return this.directions.contains(direction)
    }

    companion object {
        fun fromChar(c: Char): PipeType {
            return entries.firstOrNull { it.representation == c } ?: GROUND
        }

        fun fromDirections(directions: List<Direction>): PipeType {
            return entries.firstOrNull { it.directions.containsAll(directions) } ?: GROUND
        }
    }
}

enum class Direction(val i: Int, val j: Int) {

    NORTH(-1, 0), SOUTH(1, 0), EAST(0, 1), WEST(0, -1);

    val opposite: Direction
        get() = when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
}
