package advent.days

import advent.DayPuzzle
import advent.Position
import java.util.*

class Day14 : DayPuzzle<GroundMap>() {
    override fun parse(scanner: Scanner): GroundMap {
        val lines = mutableListOf<List<GroundType>>()
        val rollingStones = mutableListOf<Position>()
        var i = 0
        while (scanner.hasNextLine()) {
            lines += scanner.nextLine().mapIndexed { j, c ->
                val ground = GroundType.fromChar(c)
                if (ground == null) {
                    rollingStones += Position(i, j)
                    GroundType.EMPTY
                } else {
                    ground
                }
            }
            i++
        }
        return GroundMap(lines, rollingStones)
    }

    override fun solve1(input: GroundMap): String {
        println(input.printable())
        input.rollNorth()
        return input.calculateWeight().toString()
    }

    override fun solve2(input: GroundMap): String {
        println(input.printable())
        input.rollCycles(1_000_000_000)
        return input.calculateWeight().toString()
    }

}

data class Area(val start: Int, val end: Int) {
    val size = end - start + 1
}

data class GroundMap(val grid: List<List<GroundType>>, val rollingStones: StonePositions) {
    private val horizontalAreas: List<List<Area>>
    private val verticalAreas: List<List<Area>>
    private val height = grid.size

    constructor(grid: List<List<GroundType>>, rollingStones: List<Position>) : this(
        grid,
        StonePositions(mutableMapOf())
    ) {
        rollingStones.forEach { this.rollingStones.add(it) }
    }

    init {
        val horizontalAreas = mutableListOf<List<Area>>()
        val verticalAreas = mutableListOf<List<Area>>()
        for (i in grid.indices) {
            val row = grid[i]
            val horizontalAreasRow = mutableListOf<Area>()
            var start = -1
            for (j in row.indices) {
                val ground = row[j]
                if (ground == GroundType.EMPTY) {
                    if (start == -1) start = j
                } else {
                    if (start != -1) {
                        horizontalAreasRow += Area(start, j - 1)
                        start = -1
                    }
                }
            }
            if (start != -1) {
                horizontalAreasRow += Area(start, row.size - 1)
            }
            horizontalAreas += horizontalAreasRow
        }
        for (j in grid.first().indices) {
            val verticalAreasRow = mutableListOf<Area>()
            var start = -1
            for (i in grid.indices) {
                val ground = grid[i][j]
                if (ground == GroundType.EMPTY) {
                    if (start == -1) start = i
                } else {
                    if (start != -1) {
                        verticalAreasRow += Area(start, i - 1)
                        start = -1
                    }
                }
            }
            if (start != -1) {
                verticalAreasRow += Area(start, grid.size - 1)
            }
            verticalAreas += verticalAreasRow
        }
        this.horizontalAreas = horizontalAreas
        this.verticalAreas = verticalAreas
    }

    fun printable(): String = grid.joinToString("\n") { row ->
        row.joinToString("") { it.char.toString() }
    }

    fun rollNorth(north: Boolean = true) {
        val newStones = StonePositions(mutableMapOf())
        for ((j, areas) in verticalAreas.withIndex()) {
            for (area in areas) {
                val numberStonesInArea = rollingStones.countStonesInVerticalArea(area, j = j)
                if (north) {
                    newStones.addFromNorth(j = j, start = area.start, numberStones = numberStonesInArea)
                } else {
                    newStones.addFromSouth(j = j, start = area.end, numberStones = numberStonesInArea)
                }
            }
        }
        rollingStones.replaceAll(newStones)
    }

    private fun rollWest(west: Boolean = true) {
        val newStones = StonePositions(mutableMapOf())
        for ((i, areas) in horizontalAreas.withIndex()) {
            for (area in areas) {
                val numberStonesInArea = rollingStones.countStonesInHorizontalArea(area, i = i)
                if (west) {
                    newStones.addFromWest(i = i, start = area.start, numberStones = numberStonesInArea)
                } else {
                    newStones.addFromEast(i = i, start = area.end, numberStones = numberStonesInArea)
                }
            }
        }
        rollingStones.replaceAll(newStones)
    }

    fun calculateWeight(): Long {
        var weight = 0L
        for ((i, js) in rollingStones.stones.entries) {
            weight += js.size * (height - i)
        }
        return weight
    }

    fun rollCycles(nbCycles: Int) {
        for (i in 1..nbCycles) {
            rollNorth()
            rollWest()
            rollNorth(north = false)
            rollWest(west = false)
        }
    }

}

data class StonePositions(val stones: MutableMap<Int, MutableSet<Int>>) {
    fun countStonesInVerticalArea(area: Area, j: Int): Int {
        var count = 0
        for (i in area.start..area.end) {
            if (stones[i]?.contains(j) == true) count++
        }
        return count
    }

    fun countStonesInHorizontalArea(area: Area, i: Int): Int {
        var count = 0
        for (j in area.start..area.end) {
            if (stones[i]?.contains(j) == true) count++
        }
        return count
    }

    fun add(position: Position) {
        stones.getOrPut(position.i) { mutableSetOf() } += position.j
    }

    fun remove(position: Position) {
        stones[position.i]?.remove(position.j)
    }

    fun contains(position: Position): Boolean {
        return stones[position.i]?.contains(position.j) ?: false
    }

    fun addFromNorth(j: Int, start: Int, numberStones: Int) {
        for (i in start until start + numberStones) {
            add(Position(i, j))
        }
    }

    fun addFromSouth(j: Int, start: Int, numberStones: Int) {
        for (i in start downTo start - numberStones + 1) {
            add(Position(i, j))
        }
    }

    fun addFromWest(i: Int, start: Int, numberStones: Int) {
        for (j in start until start + numberStones) {
            add(Position(i, j))
        }
    }

    fun addFromEast(i: Int, start: Int, numberStones: Int) {
        for (j in start downTo start - numberStones + 1) {
            add(Position(i, j))
        }
    }

    fun replaceAll(newStones: StonePositions) {
        stones.clear()
        stones.putAll(newStones.stones)
    }
}

enum class GroundType(val char: Char) {
    EMPTY('.'), SOLID_ROCK('#');

    companion object {
        fun fromChar(char: Char): GroundType? {
            return entries.find { it.char == char }
        }
    }
}