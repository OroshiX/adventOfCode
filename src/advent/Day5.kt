package advent

import java.util.*

fun solve5(scanner: Scanner): String {
    with(scanner) {
        val listIds = mutableListOf<Int>()
        while (hasNext()) {
            val line = nextLine()
            val row = toDecimal(line.substring(0..6))
            val col = toDecimal(line.substring(7..9))
            val id = seatId(row, col)
            listIds.add(id)
        }
        return myId2(listIds).toString()
    }
}

fun maxId1(listIds: List<Int>) = listIds.max()
fun myId2(listIds: List<Int>): Int? {
    for (id in listIds) {
        if (!listIds.contains(id + 1) && listIds.contains(id + 2)) return id + 1
    }
    return null
}

fun toDecimal(fb: String): Int {
    var dec = ""
    for (c in fb) {
        when (c) {
            'F', 'L' -> dec += "0"
            'B', 'R' -> dec += "1"
        }
    }
    return Integer.parseInt(dec, 2);
}

fun seatId(row: Int, col: Int): Int = row * 8 + col