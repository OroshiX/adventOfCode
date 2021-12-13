package advent

import java.util.*

fun solve13(scanner: Scanner): String {
    with(scanner) {
        val points = mutableSetOf<Pair<Int, Int>>()
        var line = nextLine()
        while (line.isNotBlank()) {
            val (x, y) = line.split(",").map { it.toInt() }
            points += x to y
            line = nextLine()
        }
        val folds = mutableListOf<Pair<Boolean, Int>>()
        while (hasNext()) {
            val fold = nextLine()
            val (axis, num) = fold.substringAfter("fold along ").split("=")
            folds += (axis == "x") to num.toInt()
        }
        println(
            """Input:
${printable(points)}

===========================================================================================================

"""
        )
        foldStep(points, folds.first())
        println("Output:\n${printable(points)}")
        return points.size.toString()
    }
}

private fun foldStep(points: MutableSet<Pair<Int, Int>>, fold: Pair<Boolean, Int>) {
    val (axisVertical, number) = fold
    if (axisVertical) {
        // vertical
        points.filter { it.first > number }.forEach { (x, y) ->
            points.remove(x to y)
            points += 2 * number - x to y
        }
    } else {
        // horizontal
        points.filter { it.second > number }.forEach { (x, y) ->
            points.remove(x to y)
            points += x to 2 * number - y
        }
    }
}

private fun printable(points: Set<Pair<Int, Int>>): String {
    val xMax = points.maxOf { it.first }
    val yMax = points.maxOf { it.second }
    val sb = StringBuilder()
    for (y in 0..yMax) {
        for (x in 0..xMax) {
            sb.append(if (points.contains(x to y)) "#" else ".")
        }
        sb.append("\n")
    }
    return sb.toString()
}