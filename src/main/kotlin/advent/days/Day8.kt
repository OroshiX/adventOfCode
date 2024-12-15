package advent.days

import advent.DayPuzzle
import advent.Position
import java.util.*

class Day8 : DayPuzzle<AntennaGrid>() {
    override fun parse(scanner: Scanner): AntennaGrid {
        var nbLines = 0
        var nbColumns = 0
        val antenas = mutableMapOf<Char, MutableList<Position>>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            if (nbColumns == 0) {
                nbColumns = line.length
            }
            line.forEachIndexed { j, c ->
                if (c != '.') {
                    antenas.getOrPut(c) { mutableListOf() }.add(Position(nbLines, j))
                }
            }
            nbLines++
        }
        return AntennaGrid(nbLines, nbColumns, antenas)
    }

    override fun solve1(input: AntennaGrid): String {
        return input.antiNodes.values.flatten().toSet().size.toString()
    }

    override fun solve2(input: AntennaGrid): String {
        return input.antiNodesWithHarmonics.values.flatten().toSet().size.toString()
    }
}

data class AntennaGrid(
    val nbLines: Int,
    val nbColumns: Int,
    val antenas: Map<Char, List<Position>>
) {
    val antiNodes: Map<Char, Set<Position>>
        get() {
            val antinodes = mutableMapOf<Char, Set<Position>>()
            for ((type, positions) in antenas.entries) {
                val antinodesPositions = mutableSetOf<Position>()
                for (i in positions.indices) {
                    for (j in i + 1 until positions.size) {
                        val position1 = positions[i]
                        val position2 = positions[j]
                        val diffI = position1.i - position2.i
                        val diffJ = position1.j - position2.j
                        val alignedPosition1 = Position(position1.i + diffI, position1.j + diffJ)
                        val alignedPosition2 = Position(position2.i - diffI, position2.j - diffJ)
                        if (alignedPosition1.isInBounds(nbLines, nbColumns)) {
                            antinodesPositions.add(alignedPosition1)
                        }
                        if (alignedPosition2.isInBounds(nbLines, nbColumns)) {
                            antinodesPositions.add(alignedPosition2)
                        }
                    }
                }
                antinodes[type] = antinodesPositions
            }
            return antinodes
        }

    val antiNodesWithHarmonics: Map<Char, Set<Position>>
        get() {
            val antinodes = mutableMapOf<Char, Set<Position>>()
            for ((type, positions) in antenas.entries) {
                val antinodesPositions = mutableSetOf<Position>()
                for (i in positions.indices) {
                    for (j in i + 1 until positions.size) {
                        val position1 = positions[i]
                        val position2 = positions[j]
                        antinodesPositions.add(position1)
                        antinodesPositions.add(position2)
                        val diffI = position1.i - position2.i
                        val diffJ = position1.j - position2.j
                        var position = Position(position1.i + diffI, position1.j + diffJ)
                        while (position.isInBounds(nbLines, nbColumns)) {
                            antinodesPositions.add(position)
                            position = Position(position.i + diffI, position.j + diffJ)
                        }
                        position = Position(position2.i - diffI, position2.j - diffJ)
                        while (position.isInBounds(nbLines, nbColumns)) {
                            antinodesPositions.add(position)
                            position = Position(position.i - diffI, position.j - diffJ)
                        }
                    }
                }
                antinodes[type] = antinodesPositions
            }
            return antinodes
        }

    override fun toString(): String {
        val grid = Array(nbLines) { CharArray(nbColumns) { '.' } }
        for ((antenna, positions) in antenas) {
            for (position in positions) {
                grid[position.i][position.j] = antenna
            }
        }
        val nodes = antiNodesWithHarmonics.values.flatten().toSet()
        for (position in nodes) {
            if (grid[position.i][position.j] == '.') {
                grid[position.i][position.j] = '#'
            }
        }
        return grid.joinToString("\n") { it.joinToString("") }
    }
}
