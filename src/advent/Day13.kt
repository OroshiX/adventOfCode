package advent

import java.math.BigInteger
import java.util.*

fun solve13(scanner: Scanner): String {
    with(scanner) {
        nextLine()
        val buses = nextLine().split(",").toMutableList()
            .map { if (it == "x") null else it.toLong() }

        return earliestResult2(buses).toString()
    }
}

fun earliestResult2(buses: List<Long?>): BigInteger {
    // stopped at "100074294665917"
    var timestamp = BigInteger("100034027778737", 10)
    var satisfied: Boolean
    do {
        timestamp++
        satisfied = satisfied(timestamp, buses)
    } while (!satisfied)
    return timestamp
}

fun earliestResult(earliest: Int, buses: List<Int>): Int {
    var time = earliest
    val busId: Int
    l@ while (true) {
        for (id in buses) {
            if (time % id == 0) {
                busId = id
                break@l
            }
        }
        time++
    }
    return (time - earliest) * busId
}

fun satisfied(timestamp: BigInteger, buses: List<Long?>): Boolean {
    var t = timestamp
    for (id in buses) {
        if (id != null) {
            if (t.mod(BigInteger.valueOf(id)) != BigInteger.ZERO) {
                return false
            } else {
                if(true) {

                }
            }
        }
        t++
    }
    return true
}
