package advent

import java.util.*
import kotlin.math.max
import kotlin.math.min

var minX = 0
var minY = 0
var minZ = 0
var minW = 0
var maxX = 0
var maxY = 0
var maxZ = 0
var maxW = 0
fun solve17(scanner: Scanner): String {
    with(scanner) {
        val grid = mutableListOf<List<CellState>>()
        while (hasNext()) {
            grid.add(nextLine().map { if (it == '#') CellState.ACTIVE else CellState.INACTIVE })
        }
        val finalState = advanceSteps1(grid, 6)
        return nbActive(finalState).toString()
    }
}

fun nbActive(grid: Set<XYZW>): Int = grid.size
fun advanceSteps1(grid: List<List<CellState>>, steps: Int): Set<XYZW> {
    var space3D = grid.foldIndexed(mutableSetOf<XYZW>()) { y, acc, list ->
        acc.addAll(list.foldIndexed(mutableSetOf()) { x, set, cellState ->
            if (cellState == CellState.ACTIVE) set.add(XYZW(x, y, 0, 0))
            set
        })
        acc
    }.toSet()
    maxX = grid.first().size - 1
    maxY = grid.size - 1
    maxZ = 0
    System.err.println("Step 0:\n${space3D.toPrintable()}\n\n")
    for (i in 1..steps) {
        minX--
        minY--
        minZ--
        minW--
        maxX++
        maxY++
        maxZ++
        maxW++
        space3D = step(space3D)
        System.err.println("Step $i:\n${space3D.toPrintable()}\n\n")
    }
    return space3D
}

fun step(grid: Set<XYZW>): Set<XYZW> {
    val res = mutableSetOf<XYZW>()
    var miniX = 0
    var maxiX = 0
    var miniY = 0
    var maxiY = 0
    var miniZ = 0
    var maxiZ = 0
    var miniW = 0
    var maxiW = 0
    for (x in minX..maxX) {
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                for (w in minW..maxW) {
                    val neighbours = getNeighboursInSpace3D(x, y, z, w)
                    val count: Int = countActiveInGivenPositions(grid, neighbours)
                    val contains = grid.contains(XYZW(x, y, z, w))
                    val cellActive = when {
                        contains && (count == 2 || count == 3) -> true
                        contains -> false
                        !contains && count == 3 -> true
                        else -> false
                    }
                    if (cellActive) {
                        res += XYZW(x, y, z, w)
                        if (x < miniX) miniX = x
                        if (x > maxiX) maxiX = x
                        if (y < miniY) miniY = y
                        if (y > maxiY) maxiY = y
                        if (z < miniZ) miniZ = z
                        if (z > maxiZ) maxiZ = z
                        if (w < miniW) miniW = w
                        if (w > maxiW) maxiW = w
                    }
                }
            }
        }
    }
    minX = miniX
    maxX = maxiX
    minY = miniY
    maxY = maxiY
    minZ = miniZ
    maxZ = maxiZ
    minW = miniW
    maxW = maxiW
    return res
}

fun countActiveInGivenPositions(grid: Set<XYZW>, neighbours: List<XYZW>): Int {
    return neighbours.sumBy {
        if (grid.contains(it)) 1 else 0
    }
}

enum class CellState {
    INACTIVE, ACTIVE;

    override fun toString(): String {
        return when (this) {
            INACTIVE -> "."
            ACTIVE -> "#"
        }
    }
}

data class XYZW(val x: Int, val y: Int, val z: Int, val w: Int)

fun getNeighboursInSpace3D(x: Int, y: Int, z: Int, w: Int): List<XYZW> {
    val neighbors = mutableListOf<XYZW>()
    for (dx in max(minX, x - 1)..min(maxX, x + 1)) {
        for (dy in max(minY, y - 1)..min(maxY, y + 1)) {
            for (dz in max(minZ, z - 1)..min(maxZ, z + 1)) {
                for (dw in max(minW, w - 1)..min(maxW, w + 1)) {
                    neighbors.add(XYZW(dx, dy, dz, dw))
                }
            }
        }
    }
    neighbors.remove(XYZW(x, y, z, w))
    return neighbors
}

fun Set<XYZW>.toPrintable(): String {
    val sb = StringBuilder()
    for (w in minW..maxW) {
        for (z in minZ..maxZ) {
            sb.append("z = $z, w = $w\n")
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    sb.append(if (this.contains(XYZW(x, y, z, w))) '#' else '.')
                }
                sb.append("\n")
            }
            sb.append("\n\n")
        }
    }
    return sb.toString()
}