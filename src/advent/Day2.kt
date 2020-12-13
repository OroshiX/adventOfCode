package advent

import java.util.*

fun solve2(scanner: Scanner): String {
    with(scanner) {
        var valids = 0
        while (this.hasNext()) {
            val line = nextLine()
            val (minS, maxS, letter, password) = line.split("-", " ", ": ")
                .toMutableList().apply { removeIf { it.isEmpty() } }
            val p = Policy(minS.toInt(), maxS.toInt(), letter.first())
            if (p.check2(password)) {
                valids++
            }
        }
        return valids.toString()
    }
}

data class Policy(val min: Int, val max: Int, val letter: Char) {
    fun check1(password: String): Boolean {
        var num = 0
        for (c in password) {
            if (c == letter) {
                num++
            }
            if (num > max) return false
        }
        return num in min..max
    }

    fun check2(password: String): Boolean {
        val a = password[min - 1] == letter
        val b = password[max - 1] == letter
        return a xor b
    }
}