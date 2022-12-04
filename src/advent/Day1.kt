package advent

import java.util.*
class Day1 : DayPuzzle<List<List<Int>>>(1, false) {
    override fun parse(scanner: Scanner): List<List<Int>> {
        with(scanner) {
            var currentElf = mutableListOf<Int>()
            val elves = mutableListOf<List<Int>>(currentElf)
            while (this.hasNext()) {
                val nb = this.nextLine()
                if (nb.isBlank()) {
                    currentElf = mutableListOf()
                    elves.add(currentElf)
                } else {
                    currentElf.add(nb.toInt())
                }
            }
            return elves
        }
    }

    override fun solve1(input: List<List<Int>>): String {
        val sums = input.map { elf -> elf.sum() }
        return sums.max().toString()
    }

    override fun solve2(input: List<List<Int>>): String {
        val sums = input.map { elf -> elf.sum() }
        return sums.sortedDescending().take(3).sum().toString()
    }
}
