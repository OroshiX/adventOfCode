package advent.days

import advent.DayPuzzle
import advent.Position
import java.util.*
import kotlin.math.sign

class Day15 : DayPuzzle<GridWithBoxes>() {
    override fun parse(scanner: Scanner): GridWithBoxes {
        val grid = mutableListOf<List<CellType>>()
        val moves = mutableListOf<Direction>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (line.isBlank()) {
                break
            }
            grid.add(line.map { c ->
                when (c) {
                    '#' -> CellType.WALL
                    '.' -> CellType.EMPTY
                    'O' -> CellType.BOX
                    '@' -> CellType.PLAYER
                    else -> throw IllegalArgumentException("Unknown cell type $c")
                }
            })
        }
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            moves.addAll(line.map { c ->
                when (c) {
                    '^' -> Direction.UP
                    'v' -> Direction.DOWN
                    '<' -> Direction.LEFT
                    '>' -> Direction.RIGHT
                    else -> throw IllegalArgumentException("Unknown direction $c")
                }
            })
        }
        return GridWithBoxes(grid, moves)
    }

    override fun solve1(input: GridWithBoxes): String {
        println("initial: \n$input\n")
        while (input.move()) {
            println(input.toString() + "\n")
        }
        return input.calculateGPS().toString()
    }

    override fun solve2(input: GridWithBoxes): String {
        TODO()
    }
}

data class GridWithBoxes(val initialGrid: List<List<CellType>>, val moves: List<Direction>) {
    var player: Position = initialGrid.indexOfFirst { it.contains(CellType.PLAYER) }
        .let { i ->
            val j = initialGrid[i].indexOfFirst { it == CellType.PLAYER }
            Position(i, j)
        }
    val grid: MutableList<MutableList<CellType>> = initialGrid.map { it.toMutableList() }.toMutableList()
    var indexNextMove = 0

    fun move(): Boolean {
        val nextMove = moves[indexNextMove]
        fun endMove(): Boolean {
            indexNextMove++
            return indexNextMove < moves.size
        }
        // Do we have space to move?
        val nextEmptyCellPosition = nextEmptyCell(player, nextMove) ?: return endMove()
        val previous = nextMove.opposite()
        val range = nextEmptyCellPosition downUntil player
        for (pos in range) {
            // Move all the boxes on the way
            val previousPos = pos + previous
            val previousCell = grid[previousPos.i][previousPos.j]

            grid[pos.i][pos.j] = previousCell
            grid[previousPos.i][previousPos.j] = CellType.EMPTY
            if (previousCell == CellType.PLAYER) {
                player = pos
            }
        }
        return endMove()
    }

    private fun nextEmptyCell(player: Position, direction: Direction): Position? {
        var nextPosition = player

        do {
            nextPosition += direction
        } while (grid[nextPosition.i][nextPosition.j] != CellType.EMPTY && grid[nextPosition.i][nextPosition.j] != CellType.WALL)

        if (grid[nextPosition.i][nextPosition.j] == CellType.WALL) {
            return null
        }

        return nextPosition
    }

    fun calculateGPS(): Int {
        var sum = 0
        grid.forEachIndexed { i, row ->
            row.forEachIndexed { j, cell ->
                if (cell == CellType.BOX) {
                    val distance = i * 100 + j
                    sum += distance
                }
            }
        }
        return sum
    }

    override fun toString(): String {
        return "Move $indexNextMove (${moves.getOrNull(indexNextMove)?.representation()}):\n" + grid.joinToString("\n") { row ->
            row.joinToString("") { cell -> cell.representation.toString() }
        }
    }
}

private infix fun Position.downUntil(to: Position): List<Position> {
    if (this == to) return emptyList()

    val list = mutableListOf<Position>()
    val stepI = (to.i - i).sign
    val stepJ = (to.j - j).sign
    if (stepI != 0 && stepJ != 0) {
        throw IllegalArgumentException("Only horizontal or vertical ranges are supported")
    }
    var currentI = i
    var currentJ = j
    do {
        list.add(Position(currentI, currentJ))
        currentI += stepI
        currentJ += stepJ
    } while (currentI != to.i || currentJ != to.j)
    return list
}

enum class CellType(val representation: Char) {
    WALL('#'), EMPTY('.'), BOX('O'), PLAYER('@')
}

private fun Direction.opposite(): Direction = when (this) {
    Direction.UP -> Direction.DOWN
    Direction.DOWN -> Direction.UP
    Direction.LEFT -> Direction.RIGHT
    Direction.RIGHT -> Direction.LEFT
    else -> throw IllegalArgumentException("Invalid direction")
}

private fun Direction.representation(): Char = when (this) {
    Direction.UP -> '^'
    Direction.DOWN -> 'v'
    Direction.LEFT -> '<'
    Direction.RIGHT -> '>'
    else -> throw IllegalArgumentException("Invalid direction")
}
