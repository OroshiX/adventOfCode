package advent.days

import advent.DayPuzzle
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*

class Day14 : DayPuzzle<GridPositionsAndVelocities>() {
    override fun parse(scanner: Scanner): GridPositionsAndVelocities {
        val result = mutableListOf<PositionAndVelocity>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val (p, v) = line.split(" v=")
            val (px, py) = p.substringAfter("p=").split(",").map { it.toInt() }
            val (vx, vy) = v.substringAfter("v=").split(",").map { it.toInt() }
            result.add(PositionAndVelocity(Xy(px, py), Xy(vx, vy)))
        }
        return GridPositionsAndVelocities(result)
    }

    override fun solve1(input: GridPositionsAndVelocities): String {
        runBlocking {
            repeat(100) {
                input.move()
                println("\n------------\nIteration $it")
                println(input.print())
                delay(100)
            }
        }
        return input.countInQuadrants().toString()
    }

    override fun solve2(input: GridPositionsAndVelocities): String {
        val terminal = Terminal()
        runBlocking {
            repeat(10000) {
                input.move()
                if (it > 2000) {
                    terminal.danger("Iteration $it")
                    terminal.println(input.print())
                    delay(50)
                }
            }
        }
        return "See the animation above"
    }
}

data class GridPositionsAndVelocities(
    val positionsAndVelocities: List<PositionAndVelocity>,
    val widthX: Int = 101,
    val heightY: Int = 103
) {
    private val current: MutableList<PositionAndVelocity> = positionsAndVelocities.toMutableList()

    fun move() {
        for (i in current.indices) {
            val (position, velocity) = current[i]
            var x = position.x + velocity.x
            var y = position.y + velocity.y
            if (x < 0) {
                x += widthX
            } else if (x >= widthX) {
                x -= widthX
            }
            if (y < 0) {
                y += heightY
            } else if (y >= heightY) {
                y -= heightY
            }
            current[i] = PositionAndVelocity(Xy(x, y), velocity)
        }
    }

    fun countInQuadrants(): Int {
        val middle = Xy(widthX / 2, heightY / 2)
        val northWestQuadrant = current.count { it.position.x < middle.x && it.position.y < middle.y }
        val northEastQuadrant = current.count { it.position.x > middle.x && it.position.y < middle.y }
        val southWestQuadrant = current.count { it.position.x < middle.x && it.position.y > middle.y }
        val southEastQuadrant = current.count { it.position.x > middle.x && it.position.y > middle.y }
        return northEastQuadrant * northWestQuadrant * southEastQuadrant * southWestQuadrant
    }

    fun print(): String {
        val grid = Array(heightY) { CharArray(widthX) { '.' } }
        current.groupingBy { it.position }.eachCount().forEach { (position, count) ->
            grid[position.y][position.x] = count.toString().first()
        }
        return grid.joinToString("\n") { it.joinToString("") }
    }
}

data class PositionAndVelocity(val position: Xy, val velocity: Xy)

data class Xy(val x: Int, val y: Int)
