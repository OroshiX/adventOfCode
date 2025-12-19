package advent.days

import advent.DayPuzzle
import advent.terminal
import com.github.ajalt.mordant.terminal.info
import java.util.*

class Day1 : DayPuzzle<List<Rotation>>() {
    override fun parse(scanner: Scanner): List<Rotation> {
        val rotations = mutableListOf<Rotation>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine().trim()
            if (line.isNotEmpty()) {
                val directionChar = line[0]
                val degrees = line.substring(1).toInt()
                val direction = when (directionChar) {
                    'L' -> Direction.LEFT
                    'R' -> Direction.RIGHT
                    else -> throw IllegalArgumentException("Invalid direction: $directionChar")
                }
                rotations.add(Rotation(direction, degrees))
            }
        }
        return rotations
    }

    override fun solve1(input: List<Rotation>): String {
        var position = 50
        var sum0 = 0
        for (rotation in input) {
            position += rotation.degrees * rotation.direction.multiplier
            position %= 100
            if (position == 0) {
                sum0++
            }
        }
        return sum0.toString()
    }

    override fun solve2(input: List<Rotation>): String {
        var position = 50
        var res = 0
        for (rotation in input) {
            terminal.info("$position ($res) -> $rotation")
            val newPos = (position + rotation.degrees * rotation.direction.multiplier)
            when {
                newPos >= 100 -> {
                    res += newPos / 100
                    position = newPos % 100
                }

                newPos < 0 -> {
                    res += -newPos / 100 + 1
                    if (position == 0) {
                        res--
                    }
                    position = (100 + (newPos % 100)) % 100
                }

                newPos == 0 -> {
                    res++
                    position = 0
                }

                else -> {
                    position = newPos
                }
            }
        }
        return res.toString()
    }
}

data class Rotation(val direction: Direction, val degrees: Int) {
    override fun toString(): String {
        return "${direction.display} $degrees"
    }
}

enum class Direction(val multiplier: Int, val display: String = if (multiplier == -1) "L" else "R") {
    LEFT(-1), RIGHT(1)
}
