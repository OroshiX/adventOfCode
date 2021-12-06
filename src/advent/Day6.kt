package advent

import java.util.*

fun solve6(scanner: Scanner): String {
    with(scanner) {
        val list = nextLine().split(",").map { it.toInt() }
        val fishes = Fishes(list.toMutableList())

        for(i in 1..80) {
            fishes.step()
        }
        return fishes.size.toString()
    }
}

data class Fishes(val list: MutableList<Int>) {
    fun step() {
        for (i in list.indices) {
            list[i]--
            if (list[i] < 0) {
                list.add(8)
                list[i] = 6
            }
        }
    }

    val size: Int
        get() = list.size
}