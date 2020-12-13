package advent

import java.util.*

fun solve8(scanner: Scanner): String {
    with(scanner) {
        val program = mutableListOf<Instruction>()
        while (hasNext()) {
            val line = nextLine()
            val op: Operation =
                when {
                    line.startsWith("nop") -> Operation.NOP
                    line.startsWith("jmp") -> Operation.JMP
                    line.startsWith("acc") -> Operation.ACC
                    else -> throw IllegalArgumentException("illegal line $line")
                }
            val num = Regex("([+-][0-9]+)").find(line)?.value?.toInt()
                ?: throw java.lang.IllegalArgumentException("illegal line $line")
            program.add(Instruction(op, num))
        }
        return execute(program).toString()
    }
}

fun execute(program: List<Instruction>): Int {
    val linesVisited = mutableSetOf<Int>()
    var i = 0
    var acc = 0
    while (!linesVisited.contains(i)) {
        linesVisited.add(i)
        val instruction = program[i]
        when (instruction.operation) {
            Operation.ACC -> {
                acc += instruction.num
                i++
            }
            Operation.JMP -> i += instruction.num
            Operation.NOP -> i++
        }
    }
    return acc
}

data class Instruction(val operation: Operation, val num: Int)

enum class Operation {
    ACC, JMP, NOP
}