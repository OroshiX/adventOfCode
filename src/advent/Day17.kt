package advent

import java.util.*
import kotlin.math.max
import kotlin.math.min

var minX = 0
var minY = 0
var minZ = 0
var maxX = 0
var maxY = 0
var maxZ = 0
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

fun nbActive(grid: Set<XYZ>): Int = grid.size
fun advanceSteps1(grid: List<List<CellState>>, steps: Int): Set<XYZ> {
    var space3D = grid.foldIndexed(mutableSetOf<XYZ>()) { y, acc, list ->
        acc.addAll(list.foldIndexed(mutableSetOf()) { x, set, cellState ->
            if (cellState == CellState.ACTIVE) set.add(XYZ(x, y, 0))
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
        maxX++
        maxY++
        maxZ++
        space3D = step(space3D)
        System.err.println("Step $i:\n${space3D.toPrintable()}\n\n")
    }
    return space3D
}

fun step(grid: Set<XYZ>): Set<XYZ> {
    val res = mutableSetOf<XYZ>()
    var miniX = 0
    var maxiX = 0
    var miniY = 0
    var maxiY = 0
    var miniZ = 0
    var maxiZ = 0
    for (x in minX..maxX) {
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                val neighbours = getNeighboursInSpace3D(x, y, z, minX, maxX, minY, maxY, minZ, maxZ)
                val count: Int = countActiveInGivenPositions(grid, neighbours)
                val contains = grid.contains(XYZ(x, y, z))
                val cellActive = when {
                    contains && (count == 2 || count == 3) -> true
                    contains -> false
                    !contains && count == 3 -> true
                    else -> false
                }
                if (cellActive) {
                    res += XYZ(x, y, z)
                    if (x < miniX) miniX = x
                    if (x > maxiX) maxiX = x
                    if (y < miniY) miniY = y
                    if (y > maxiY) maxiY = y
                    if (z < miniZ) miniZ = z
                    if (z > maxiZ) maxiZ = z
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
    return res
}

fun countActiveInGivenPositions(grid: Set<XYZ>, neighbours: List<XYZ>): Int {
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

data class XYZ(val x: Int, val y: Int, val z: Int)

fun getNeighboursInSpace3D(x: Int, y: Int, z: Int, minX: Int, maxX: Int, minY: Int, maxY: Int, minZ: Int, maxZ: Int): List<XYZ> {
    val neighbors = mutableListOf<XYZ>()
    for (dx in max(minX, x - 1)..min(maxX, x + 1)) {
        for (dy in max(minY, y - 1)..min(maxY, y + 1)) {
            for (dz in max(minZ, z - 1)..min(maxZ, z + 1)) {
                neighbors.add(XYZ(dx, dy, dz))
            }
        }
    }
    neighbors.remove(XYZ(x, y, z))
    return neighbors
}

fun Set<XYZ>.toPrintable(): String {
    val sb = StringBuilder()
    for (z in minZ..maxZ) {
        sb.append("z = $z\n")
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                sb.append(if (this.contains(XYZ(x, y, z))) '#' else '.')
            }
            sb.append("\n")
        }
        sb.append("\n\n")
    }
    return sb.toString()
}