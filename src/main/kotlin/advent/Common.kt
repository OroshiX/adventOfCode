package advent

data class Position(val i: Int, val j: Int) {
    fun isInBounds(nbLines: Int, nbCols: Int): Boolean {
        return i in 0 until nbLines && j in 0 until nbCols
    }

    operator fun plus(other: Position): Position {
        return Position(i + other.i, j + other.j)
    }

    operator fun plus(direction: Direction): Position {
        return Position(i + direction.di, j + direction.dj)
    }
}

enum class Direction(val di: Int, val dj: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1);

    companion object {
        val cross = listOf(UP, DOWN, LEFT, RIGHT)
    }

    fun rotateLeft(): Direction {
        return when (this) {
            UP -> LEFT
            LEFT -> DOWN
            DOWN -> RIGHT
            RIGHT -> UP
            else -> throw IllegalArgumentException("Invalid direction")
        }
    }

    fun rotateRight(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            else -> throw IllegalArgumentException("Invalid direction")
        }
    }
}
