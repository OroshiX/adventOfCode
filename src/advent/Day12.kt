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
        return manhattan(Pair(0, 0), moveShip2(actions)).toString()
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

fun moveShip2(actions: List<Action>): Pair<Int, Int> {
    var waypointPosition = Pair(10, 1)
    var shipPosition = Pair(0, 0)
    for (a in actions) {
        val (sp, wp) = stepMoveShip2(a, waypointPosition, shipPosition)
        shipPosition = sp
        waypointPosition = wp
    }
    return shipPosition
}

fun stepMoveShip2(
    action: Action,
    waypointPosition: Pair<Int, Int>,
    shipPosition: Pair<Int, Int>
): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    var endShipPosition: Pair<Int, Int> = shipPosition
    var endWaypointPosition: Pair<Int, Int> = waypointPosition

    when (action.name) {
        NameAction.L -> {
            var numRotations = (action.num % 360) / 90
            while (numRotations > 0) {
                endWaypointPosition = rotateLeft2(endWaypointPosition)
                numRotations--
            }
        }
        NameAction.R -> {
            var numRotations = (action.num % 360) / 90
            while (numRotations > 0) {
                endWaypointPosition = rotateRight2(endWaypointPosition)
                numRotations--
            }
        }
        NameAction.F -> {
            var numForward = action.num
            while (numForward > 0) {
                endShipPosition = Pair(
                    waypointPosition.first + endShipPosition.first,
                    waypointPosition.second + endShipPosition.second
                )
                numForward--
            }
        }
        else -> endWaypointPosition =
            moveInCardinalPos(action.name, action.num, waypointPosition)
    }

    return Pair(endShipPosition, endWaypointPosition)
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

private fun rotateLeft2(waypointPosition: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(-waypointPosition.second, waypointPosition.first)
}

private fun rotateRight2(waypointPosition: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(waypointPosition.second, -waypointPosition.first)
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