package advent

import java.util.*

fun solve6(scanner: Scanner): String {
    with(scanner) {
        val group = mutableListOf<String>()
        var res = 0
        while (hasNext()) {
            val line = nextLine()
            if (line.isEmpty()) {
                if (group.isNotEmpty()) {
                    res += allAnswersInGroup2(group)
                }
                group.clear()
                continue
            }
            group.add(line)
        }
        res += allAnswersInGroup2(group)
        return res.toString()
    }
}

fun differentAnswersInGroup1(answers: List<String>): Int {
    val set = mutableSetOf<Char>()
    for (answer in answers) {
        set.addAll(answer.asIterable())
    }
    return set.size
}

fun allAnswersInGroup2(answers: List<String>): Int {
    var set = mutableSetOf<Char>()
    set.addAll(answers.first().asIterable())
    for (i in 1 until answers.size) {
        set = set.intersect(answers[i].asIterable()).toMutableSet()
    }
    return set.size
}