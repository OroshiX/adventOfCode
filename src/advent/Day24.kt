package advent

import java.util.*
import kotlin.collections.ArrayList

fun solve24(scanner: Scanner): String {
    with(scanner) {
        val flipped = SortedArrayList<HexCoordinate>(
            Comparator { o1: HexCoordinate, o2: HexCoordinate ->
                if (o1.y > o2.y) return@Comparator 1
                if (o1.y < o2.y) return@Comparator -1
                ((o1.x - o2.x) * 2)
            })
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
        System.err.println(flipped.toPrintable())
        flip2(flipped)
        return flipped.size.toString()
    }
}

private fun flip2(flipped: SortedMutableList<HexCoordinate>) {
    for (i in 1..100) {
        flipped.step()
        System.err.println("Day $i: ${flipped.size}")
        System.err.println(flipped.toPrintable())
    }
}

fun SortedMutableList<HexCoordinate>.step() {
    val minY = first().y - 1
    val maxY = last().y + 1
    val minX = minBy { it.x }!!.x - 1
    val maxX = maxBy { it.x }!!.x + 1
    val toRemove = mutableListOf<HexCoordinate>()
    val toAdd = mutableListOf<HexCoordinate>()
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            val current = HexCoordinate(x, y)
            val black = contains(current)
            val count: Int = this.countNeighbours(current)
            when {
                black && (count == 0 || count > 2) -> toRemove += current
                !black && count == 2 -> toAdd += current
            }
        }
    }
    for (r in toRemove) {
        remove(r)
    }
    for (a in toAdd) {
        add(a)
    }
}

private fun SortedMutableList<HexCoordinate>.countNeighbours(
    current: HexCoordinate): Int {
    var count = 0
    if (contains(HexCoordinate(current.x - 2, current.y))) count++
    if (contains(HexCoordinate(current.x + 2, current.y))) count++
    if (contains(HexCoordinate(current.x - 1, current.y - 1))) count++
    if (contains(HexCoordinate(current.x - 1, current.y + 1))) count++
    if (contains(HexCoordinate(current.x + 1, current.y - 1))) count++
    if (contains(HexCoordinate(current.x + 1, current.y + 1))) count++
    return count
}

fun SortedMutableList<HexCoordinate>.toPrintable(): String {
    val minY = first().y
    val maxY = last().y
    val minX = minBy { it.x }!!.x
    val maxX = maxBy { it.x }!!.x
    val sb = StringBuilder()
    for (y in minY..maxY) {
        if (y % 2 == 0) sb.append(' ')
        for (x in minX..maxX) {
            sb.append(
                if (contains(HexCoordinate(x, y))) '#' else '.'
            ).append(' ')
        }
        sb.append('\n')
    }
    return sb.toString()
}

data class HexCoordinate(var x: Int = 0, var y: Int = 0) {
    fun applyDirection(direction: String) {
        when (direction) {
            "e" -> x += 2
            "se" -> {
                x++
                y--
            }
            "sw" -> {
                x--
                y--
            }
            "w" -> x -= 2
            "nw" -> {
                x--
                y++
            }
            "ne" -> {
                x++
                y++
            }
        }
    }
}

interface SortedMutableList<T> : Iterable<T> {
    val size: Int
    fun add(element: T)
    fun remove(element: T)
    operator fun get(index: Int): T
    operator fun contains(element: T): Boolean
}

class SortedArrayList<T>(private val comp: Comparator<T>) :
    SortedMutableList<T> {
    private val list: ArrayList<T> = ArrayList()

    constructor(vararg elements: T, comp: Comparator<T>) : this(comp) {
        list.addAll(elements)
    }

    override val size: Int
        get() = list.size

    override fun add(element: T) = findIndex(element).let { index ->
        list.add(
            if (index < 0) -(index + 1) else index, element
        )
    }

    override fun remove(element: T) =
        findIndex(element).let { index -> if (index >= 0) list.removeAt(index) }

    override fun get(index: Int): T = list[index]

    override fun contains(element: T) = findIndex(element).let { index ->
        index >= 0 && element == list[index] || (findEquals(
            index + 1, element, 1
        ) || findEquals(index - 1, element, -1))
    }

    override fun iterator(): Iterator<T> = list.iterator()

    private fun findIndex(element: T): Int = list.binarySearch(element, comp)

    private tailrec fun findEquals(index: Int, element: T, step: Int): Boolean =
        when {
            index !in 0 until size -> false
            comp.compare(element, list[index]) != 0 -> false
            list[index] == element -> true
            else -> findEquals(index + step, element, step)
        }
}

fun <T : Comparable<T>> sortedMutableListOf(
    vararg elements: T): SortedMutableList<T> =
    SortedArrayList(*elements, comp = compareBy { it })

fun <T> sortedMutableListOf(comparator: Comparator<T>,
                            vararg elements: T): SortedMutableList<T> =
    SortedArrayList(elements = *elements, comp = comparator)