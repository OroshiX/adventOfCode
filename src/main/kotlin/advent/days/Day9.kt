package advent.days

import advent.DayPuzzle
import java.util.*

class Day9 : DayPuzzle<Disk>() {
    override fun parse(scanner: Scanner): Disk {
        val line = scanner.nextLine()
        var id = 0
        val diskSpaces = mutableListOf<DiskElement>()
        var position = 0
        var occupied = true
        line.forEach { c ->
            c.digitToInt().let { size ->
                if (size > 0) {
                    diskSpaces.add(
                        if (occupied) {
                            DiskSpace(
                                id = id,
                                position = position,
                                size = size,
                            )
                        } else {
                            DiskEmpty(
                                position = position,
                                size = size
                            )
                        }
                    )
                }
                position += size
            }
            occupied = !occupied
            if (occupied) {
                id++
            }
        }
        return Disk(diskSpaces)
    }

    override fun solve1(input: Disk): String {
        return input.checkSum(input.compactDisk).toString()
    }

    override fun solve2(input: Disk): String {
        TODO()
    }
}

sealed class DiskElement(open val position: Int, open val size: Int, val occupied: Boolean)
data class DiskSpace(val id: Int, override val position: Int, override val size: Int) :
    DiskElement(position, size, true)

data class DiskEmpty(override val position: Int, override val size: Int) :
    DiskElement(position, size, false)

data class Disk(val diskSpaces: List<DiskElement>) {
    val compactDisk: List<Long>
        get() {
            val allSpaces = diskSpaces.flatMap { diskElement ->
                if (diskElement is DiskSpace) {
                    List(diskElement.size) { diskElement.id.toLong() }
                } else {
                    List(diskElement.size) { null }
                }
            }.toMutableList()

            while (allSpaces.indexOfLast { it == null } != allSpaces.indexOfFirst { it == null }) {
                // Get the last element and put it at the 1st null position
                val lastFilledIndex = allSpaces.indexOfLast { it != null }
                val lastFilled = allSpaces[lastFilledIndex]
                allSpaces[lastFilledIndex] = null
                val firstNullIndex = allSpaces.indexOfFirst { it == null }
                allSpaces[firstNullIndex] = lastFilled

                // Remove all nulls at the end
                val lastNullIndex = allSpaces.lastPortionNullIndex()
                allSpaces.subList(lastNullIndex, allSpaces.size).clear()
            }
            return allSpaces.filterNotNull()
        }

    private fun List<Long?>.lastPortionNullIndex(): Int {
        var res = -1
        for (i in size - 1 downTo 0) {
            if (this[i] == null) {
                res = i
            } else {
                break
            }
        }
        return res
    }

    fun checkSum(compacted: List<Long?>): Long =
        compacted.foldIndexed(0L) { index, acc, id ->
            acc + (id ?: 0) * index
        }

    override fun toString(): String {
        return diskSpaces.joinToString("") {
            if (it is DiskSpace) {
                it.id.toString()
            } else {
                "."
            }.repeat(it.size)
        }
    }
}
