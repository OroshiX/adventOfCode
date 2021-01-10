package advent

import java.util.*

fun solve24(scanner: Scanner): String {
    with(scanner) {
        val flipped = mutableSetOf<HexCoordinate>()
        while (hasNext()) {
            val tile = HexCoordinate()
            val line = nextLine()
            val matchResult = Regex("(se|e|sw|w|nw|ne)").findAll(line)
            matchResult.forEach {
                tile.applyDirection(it.value)
            }
            if (flipped.contains(tile)) {
                flipped.remove(tile)
            } else {
                flipped.add(tile)
            }
        }
        return flipped.size.toString()
    }
}

data class HexCoordinate(var x: Float = 0f, var y: Float = 0f) {
    fun applyDirection(direction: String) {
        when (direction) {
            "e" -> x++
            "se" -> {
                x += 0.5f
                y -= 0.5f
            }
            "sw" -> {
                x -= 0.5f
                y -= 0.5f
            }
            "w" -> x--
            "nw" -> {
                x -= 0.5f
                y += 0.5f
            }
            "ne" -> {
                x += 0.5f
                y += 0.5f
            }
        }
    }
}