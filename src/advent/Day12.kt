package advent

import java.util.*
import kotlin.math.abs

fun solve12(scanner: Scanner): String {
    with(scanner) {
        val actions = mutableListOf<Action>()
        while (hasNext()) {
            val line = nextLine()
            actions.add(
                Action(line.substring(1).toInt(), NameAction.fromName(line[0]))
            )
        }
        return manhattan(Pair(0, 0), moveShip(actions)).toString()
    }
}

fun manhattan(origin: Pair<Int, Int>, end: Pair<Int, Int>): Int =
    abs(origin.first - end.first) + abs(origin.second - end.second)

fun moveShip(actions: List<Action>): Pair<Int, Int> {
    var pair = Pair(0, 0)
    var facing = NameAction.E
    for (a in actions) {
        val (f, p) = stepMoveShip(a, facing, pair)
        facing = f
        pair = p
    }
    return pair
}

fun stepMoveShip(
    action: Action,
    facing: NameAction,
    position: Pair<Int, Int>
): Pair<NameAction, Pair<Int, Int>> {
    var facingEnd: NameAction = facing
    var positionEnd: Pair<Int, Int> = position
    when (action.name) {
        NameAction.L -> {
            var numRotations = (action.num % 360) / 90
            while (numRotations > 0) {
                facingEnd = rotateLeft(facingEnd)
                numRotations--
            }
        }
        NameAction.R -> {
            var numRotations = (action.num % 360) / 90
            while (numRotations > 0) {
                facingEnd = rotateRight(facingEnd)
                numRotations--
            }
        }
        NameAction.F -> positionEnd =
            moveInCardinalPos(facing, action.num, position)
        else -> positionEnd =
            moveInCardinalPos(action.name, action.num, position)
    }
    return Pair(facingEnd, positionEnd)
}

private fun rotateLeft(facing: NameAction): NameAction {
    return when (facing) {
        NameAction.N -> NameAction.W
        NameAction.S -> NameAction.E
        NameAction.E -> NameAction.N
        NameAction.W -> NameAction.S
        else -> throw IllegalArgumentException()
    }
}

private fun rotateRight(facing: NameAction): NameAction {
    return when (facing) {
        NameAction.N -> NameAction.E
        NameAction.S -> NameAction.W
        NameAction.E -> NameAction.S
        NameAction.W -> NameAction.N
        else -> throw IllegalArgumentException()
    }
}

private fun moveInCardinalPos(
    direction: NameAction,
    num: Int,
    position: Pair<Int, Int>
): Pair<Int, Int> {
    return when (direction) {
        NameAction.N -> Pair(position.first, position.second + num)
        NameAction.S -> Pair(position.first, position.second - num)
        NameAction.E -> Pair(position.first + num, position.second)
        NameAction.W -> Pair(position.first - num, position.second)
        else -> throw IllegalArgumentException("Cardinal position $direction illegal")
    }
}

enum class NameAction(val value: Char) {
    N('N'), S('S'), E('E'), W('W'), L('L'), R('R'), F('F');

    companion object {
        fun fromName(name: Char): NameAction {
            return values().find { it.value == name }
                ?: throw IllegalArgumentException("name $name unrecognized")
        }
    }
}

data class Action(val num: Int, val name: NameAction)