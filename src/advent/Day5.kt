package advent

import java.util.*

class Day5 : DayPuzzle<Stacks>(5, false) {
    override fun parse(scanner: Scanner): Stacks {
        val columns: List<Deque<String>>

        var input = scanner.nextLine()
        val length = input.length
        columns = List((input.length + 1) / 4) { ArrayDeque() }
        while (parseLine(input, length) { col, char ->
                columns[col].add("$char")
            }) {
            input = scanner.nextLine()
        }
        val moves = mutableListOf<MoveAction>()
        while (scanner.hasNext()) {
            moves += MoveAction(scanner.nextLine())
        }

        return Stacks(columns.mapIndexed { index, strings -> index + 1 to strings }.toMap(), moves)
    }

    private fun parseLine(input: String, length: Int, addToColumn: (Int, Char) -> Unit): Boolean {
        if (input.length != length) return false
        for (i in input.indices) {
            val c = input[i]
            if (i % 4 == 1 && c.isLetter()) addToColumn(i / 4, c)
        }
        return true
    }

    override fun solve1(input: Stacks): String {
        for (action in input.moves) {
            input.move(action)
        }
        return input.topStacks()
    }

    override fun solve2(input: Stacks): String {
        for (action in input.moves) {
            input.move2(action)
        }
        return input.topStacks()
    }
}

data class Stacks(val stacks: Map<Int, Deque<String>>, val moves: List<MoveAction>) {
    private fun move(from: Int, to: Int) {
        val popped = stacks.getValue(from).pop()
        stacks.getValue(to).push(popped)
    }

    fun move(moveAction: MoveAction) {
        for (i in 1..moveAction.number) {
            move(moveAction.from, moveAction.to)
        }
    }

    fun move2(moveAction: MoveAction) {
        val popped = mutableListOf<String>()
        for (i in 1..moveAction.number) {
            popped += stacks.getValue(moveAction.from).pop()
        }
        for (pop in popped.reversed()) {
            stacks.getValue(moveAction.to).push(pop)
        }
    }

    fun topStacks(): String {
        val keys = stacks.keys.sorted()
        val sb = StringBuilder()
        for (key in keys) {
            sb.append(stacks[key]?.peek())
        }
        return sb.toString()
    }
}

data class MoveAction(val from: Int, val to: Int, val number: Int) {
    constructor(input: String) : this(
        input.split(" ")[3].toInt(), input.split(" ")[5].toInt(), input.split(" ")[1].toInt()
    )
}
