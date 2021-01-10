package advent

import java.util.*

fun solve25(scanner: Scanner): String {
    with(scanner) {
        while (hasNext()) {
            println(nextLine())
        }
    }
    return ""
}

private fun handshake(loopSize: Int, subjectNumber: Int): Int {
    var current = 1
    for (i in 0 until loopSize) {
        current *= subjectNumber
        current = current.rem(20201227)
    }
    return current
}