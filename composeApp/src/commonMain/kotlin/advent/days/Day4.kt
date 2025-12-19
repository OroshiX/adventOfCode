package advent.days

import advent.DayPuzzle
import java.util.*

class Day4 : DayPuzzle<ToiletPaperGrid>() {
    override fun parse(scanner: Scanner): ToiletPaperGrid {
        val grid = mutableListOf<List<Boolean>>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine().trim()
            if (line.isNotEmpty()) {
                val row = line.map { it == '@' }
                grid.add(row)
            }
        }
        return ToiletPaperGrid(grid)
    }

    override fun solve1(input: ToiletPaperGrid): String {
        var accessibleCount = 0
        for (i in input.grid.indices) {
            for (j in input.grid[0].indices) {
                if (input.grid[i][j] && input.isAccessible(i, j)) {
                    accessibleCount++
                }
            }
        }
        return accessibleCount.toString()
    }

    override fun solve2(input: ToiletPaperGrid): String {
        TODO()
    }
}

data class ToiletPaperGrid(val grid: List<List<Boolean>>) {
    fun isAccessible(i: Int, j: Int): Boolean {
        var sum = 0
        for (di in -1..1) {
            for (dj in -1..1) {
                if (di == 0 && dj == 0) continue
                val ni = i + di
                val nj = j + dj
                if (ni in grid.indices && nj in grid[0].indices && grid[ni][nj]) {
                    sum++
                }
            }
        }
        return sum < 4
    }
}
