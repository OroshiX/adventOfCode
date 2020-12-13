package advent

import java.util.*

const val PREV = 25
fun solve9(scanner: Scanner): String {
    with(scanner) {
        val nums = mutableListOf<Long>()
//        var lineNumber = 0
        while (hasNext()) {
            val n = nextLine().toLong()
            nums.add(n)
//            if (lineNumber >= PREV) {
//                if (!checkValue(nums, lineNumber)) {
//                    return findContiguous(n, nums).toString()
//                }
//            }
//            lineNumber++
        }
        val invalid = findInvalid(nums) ?: throw IllegalStateException()
        return findContiguous(invalid, nums).toString()
    }
}

fun findContiguous(n: Long, nums: List<Long>): Long? {
    for (i in nums.indices) {
        var sum: Long = 0
        var j = i
        val list = mutableListOf<Long>()
        while (sum < n) {
            sum += nums[j]
            list.add(nums[j])
            j++
        }
        if (sum == n && j != i) {
            // found
            return list.min()!! + list.max()!!
        }
    }
    return null
}

fun findInvalid(nums: List<Long>): Long? {
    for (n in nums.withIndex()) {
        if (n.index >= PREV) {
            if (!checkValue(nums, n.index)) return n.value
        }
    }
    return null
}

fun checkValue(nums: List<Long>, lineNumber: Int): Boolean {
    val sum = nums[lineNumber]
    for (i in lineNumber - PREV until lineNumber) {
        for (j in i + 1 until lineNumber) {
            if (nums[i] + nums[j] == sum) return true
        }
    }
    return false
}
