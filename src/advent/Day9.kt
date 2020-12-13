package advent

import java.util.*

const val PREV = 25
fun solve9(scanner: Scanner): String {
    with(scanner) {
        val nums = mutableListOf<Int>()
        var lineNumber = 0
        while (hasNext()) {
            val n = nextLine().toInt()
            nums.add(n)
            if (lineNumber >= PREV) {
                if (!checkValue(nums, lineNumber)) {
                    return n.toString()
                }
            }
            lineNumber++
        }
        throw IllegalStateException()
    }
}

fun checkValue(nums: List<Int>, lineNumber: Int): Boolean {
    val sum = nums[lineNumber]
    for (i in lineNumber - PREV until lineNumber) {
        for (j in i + 1 until lineNumber) {
            if (nums[i] + nums[j] == sum) return true
        }
    }
    return false
}
