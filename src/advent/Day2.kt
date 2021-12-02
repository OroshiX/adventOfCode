package advent

import java.util.*

fun solve2(scanner: Scanner): String {
    with(scanner) {
        val instructions = mutableListOf<String>()
        while (hasNext()) {
            instructions.add(nextLine())
        }
        return applyInstructions(instructions).toString()
    }
}

fun applyInstructions(instructions: List<String>): Int {
    val pos = Position()
    instructions.forEach { pos.applyMovement(it) }
    return pos.multiply()
}

private data class Position(var horizontal: Int = 0, var vertical: Int = 0) {
    fun applyMovement(instruction: String): Unit {
        val (dir, stepString) = instruction.split(" ")
        val step = stepString.toInt()
        when (dir) {
            "forward" -> horizontal += step
            "down" -> vertical += step
            "up" -> vertical -= step
        }
    }

    fun multiply(): Int = horizontal * vertical
}