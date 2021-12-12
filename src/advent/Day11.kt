package advent

import java.util.*

fun solve11(scanner: Scanner): String {
    with(scanner) {
        val grid = mutableListOf<MutableList<Int>>()
        while (hasNext()) {
            grid += nextLine().map { Character.getNumericValue(it) }.toMutableList()
        }
        return Grid11(grid).part1().toString()
    }
}


private data class Grid11(val grid: MutableList<MutableList<Int>>) {
    val iMax = grid.size - 1
    val jMax = grid.first().size - 1

    fun part1(): Int {
        var flashes = 0
        for (i in 0 until 100) {
            flashes += step()
        }
        return flashes
    }


    private fun step(): Int {
        val toFlash = mutableListOf<Pair<Int, Int>>()
        // increase 1 everywhere
        for (i in 0..iMax) {
            for (j in 0..jMax) {
                grid[i][j]++
                if (grid[i][j] > 9) {
                    toFlash += i to j
                }
            }
        }
        val flashed = flash(toFlash, mutableListOf(), mutableSetOf())

        // reset flashed to 0
        for ((i, j) in flashed) {
            grid[i][j] = 0
        }
        return flashed.size
    }

    private fun flash(
        leftToTreat: List<Pair<Int, Int>>,
        treated: MutableList<Pair<Int, Int>>,
        flashed: MutableSet<Pair<Int, Int>>
    ): MutableSet<Pair<Int, Int>> {
        if (leftToTreat.isEmpty()) return flashed
        val toTreat = mutableListOf<Pair<Int, Int>>()
        for ((i, j) in leftToTreat) {
            if (flashed.contains(i to j)) {
                continue
            }
            flashed += i to j
            for (ni in i - 1..i + 1) {
                for (nj in j - 1..j + 1) {
                    val neighbour = ni to nj
                    if (neighbour.inBounds() && neighbour != i to j) {
                        if (!flashed.contains(neighbour)) {
                            grid[ni][nj]++
                            if (grid[ni][nj] > 9) {
                                toTreat += neighbour
                            }
                        }
                    }
                }
            }
            treated += i to j

        }
        return flash(toTreat, treated, flashed)
    }

    override fun toString(): String = grid.fold("") { acc, row ->
        "$acc\n${row.fold("") { accRow, e -> "$accRow${if (e > 9) 0 else e}" }}"
    }

    private fun Pair<Int, Int>.inBounds(): Boolean {
        val (i, j) = this
        return i in 0..iMax && j in 0..jMax
    }
}
