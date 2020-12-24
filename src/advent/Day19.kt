package advent

import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    solve19(scanner)
}

fun solve19(scanner: Scanner) {
    with(scanner) {
        val testNumbers = nextLine().toInt()
        for (i in 0 until testNumbers) {
            val n = nextLine().toInt()
            val sequence = nextLine().split(" ").map { it.toInt() }

            println(tupleDiff(sequence))
        }
    }
}

fun tupleDiff(list: List<Int>): Int {
    TODO("Not yet implemented")
}