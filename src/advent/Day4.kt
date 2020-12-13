package advent

import java.util.*

fun solve4(scanner: Scanner): String {
    with(scanner) {
        val passport = mutableMapOf<String, String>()
        var nbValid = 0
        while (hasNext()) {
            val line = nextLine()
            if (line.isEmpty()) {
                if (passport.isNotEmpty()) {
                    if (isValid(passport)) {
                        nbValid++
                    }
                }
                passport.clear()
                continue
            }
            val keyValues = line.split(" ")
            for (kv in keyValues) {
                val (key, value) = kv.split(":")
                passport[key] = value
            }
        }
        if (isValid(passport)) {
            nbValid++
        }
        return nbValid.toString()
    }
}

val mandatory =
    listOf(
        Pair("byr", Regex("(19[2-9][0-9]|200[0-2])")),
        Pair("iyr", Regex("20(1[0-9]|20)")),
        Pair("eyr", Regex("20(2[0-9]|30)")),
        Pair("hgt", Regex("(1([5-8][0-9]|9[0-3])cm|(59|6[0-9]|7[0-6])in)")),
        Pair("hcl", Regex("#[0-9a-f]{6}")),
        Pair("ecl", Regex("(amb|blu|brn|gry|grn|hzl|oth)")),
        Pair("pid", Regex("[0-9]{9}"))
    )
val optional = listOf("cid")

fun isValid(passport: Map<String, String>): Boolean {
    for (m in mandatory) {
        if (!passport.containsKey(m.first)) return false
        if (!m.second.matches(passport[m.first]!!)) return false
    }
    return true
}