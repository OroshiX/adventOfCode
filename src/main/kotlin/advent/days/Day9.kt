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
        return input.checkSum(input.compactDisk2).toString()
    }
}

sealed class DiskElement(open val position: Int, open val size: Int)
data class DiskSpace(val id: Int, override val position: Int, override val size: Int) :
    DiskElement(position, size)

data class DiskEmpty(override val position: Int, override val size: Int) :
    DiskElement(position, size)

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


    val compactDisk2: List<Long?>
        get() {
            var allSpaces = diskSpaces.flatMap { diskElement ->
                if (diskElement is DiskSpace) {
                    List(diskElement.size) { diskElement.id.toLong() }
                } else {
                    List(diskElement.size) { null }
                }
            }.toMutableList()
            val emptySpaces = diskSpaces.filterIsInstance<DiskEmpty>().toMutableList()
            val toMoveList = diskSpaces.filterIsInstance<DiskSpace>().reversed().toMutableList()

            for (toMove in toMoveList) {
                if (emptySpaces.isEmpty()) {
                    break
                }
                var indexEmpty = 0
                var empty: DiskEmpty = emptySpaces[indexEmpty]
                while (indexEmpty < emptySpaces.size) {
                    empty = emptySpaces[indexEmpty]
                    if (empty.size >= toMove.size) {
                        break
                    }
                    indexEmpty++
                }
                if (toMove.size <= empty.size && toMove.position > empty.position) {
                    // Move toMove to empty
                    for (i in 0 until toMove.size) {
                        val element = allSpaces[toMove.position + i]
                        allSpaces[empty.position + i] = element
                        allSpaces[toMove.position + i] = null
                    }

                    allSpaces.lastPortionNullIndex().let {
                        if (it != -1) {
                            allSpaces = allSpaces.dropLast(allSpaces.size - it).toMutableList()
                        }
                    }
                    empty = DiskEmpty(empty.position + toMove.size, empty.size - toMove.size)
                    if (empty.size > 0) {
                        emptySpaces[indexEmpty] = empty
                    } else {
                        emptySpaces.removeAt(indexEmpty)
                    }
                }
            }
            println(allSpaces.prettyPrint())
            return allSpaces
        }

    fun List<Long?>.prettyPrint(): String {
        return joinToString("") {
            if (it == null) {
                return@joinToString "."
            }
            val id = it % 222
            (48 + id).toInt().toChar().toString()
        }
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
                // Get the char of the id
                // Only loop from 32 to 126 character code with modulo
                val id = it.id % 95
                (32 + id).toChar().toString()
            } else {
                "."
            }.repeat(it.size)
        }
    }
}
