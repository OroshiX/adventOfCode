package advent

import java.util.*

fun solve9(scanner: Scanner): String {
    with(scanner) {
        val grid = mutableListOf<List<Int>>()
        while (hasNext()) {
            grid += nextLine().map { Character.getNumericValue(it) }
        }
        return localMinimums(grid).toString()
    }
}

private fun localMinimums(grid: List<List<Int>>): Int {
    return grid.foldIndexed(0) { i, acc, row ->
        acc + row.foldIndexed(0) { j, sum, number ->
            var ok = true
            val neighbors =
                mutableListOf(i - 1 to j, i + 1 to j, i to j - 1, i to j + 1).filter { pair ->
                    pair.first >= 0 && pair.second >= 0 && pair.first < grid.size && pair.second < row.size
                }

            for ((ii, jj) in neighbors) {
                if (grid[ii][jj] <= number) {
                    ok = false
                    break
                }
            }
            sum + (if (ok) number + 1 else 0)
        }
    }
}