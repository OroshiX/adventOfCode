package advent

import java.util.*

fun solve16(scanner: Scanner): String {
    val notes = readInputNotes(scanner)
    return departureFields2(notes).toString()
}

val ruleRegex =
        Regex("(?<field>[\\w ]+): (?<range1a>[0-9]+)-(?<range1b>[0-9]+) or (?<range2a>[0-9]+)-(?<range2b>[0-9]+)")

fun treatRuleInput(line: String): RulesTicket {
    val match = ruleRegex.matchEntire(line)
            ?: throw IllegalArgumentException("line $line not allowed")
    return RulesTicket(
            match.groups["field"]!!.value,
            match.groups["range1a"]!!.value.toInt()..match.groups["range1b"]!!.value.toInt(),
            match.groups["range2a"]!!.value.toInt()..match.groups["range2b"]!!.value.toInt()
    )
}

fun readInputNotes(scanner: Scanner): MyNotes {
    with(scanner) {
        val rules = mutableListOf<RulesTicket>()
        var line = nextLine()
        // read rules
        while (line.isNotEmpty()) {
            val rulesTicket = treatRuleInput(line)
            rules.add(rulesTicket)
            line = nextLine()
        }
        // read my ticket
        nextLine()
        val myTicket = nextLine().split(",").map { it.toInt() }
        nextLine()
        // nearby tickets
        val others = mutableListOf<List<Int>>()
        nextLine()
        while (hasNext()) {
            val nearbyTicket = nextLine().split(",").map { it.toInt() }
            others.add(nearbyTicket)
        }
        return MyNotes(rules, myTicket, others)
    }
}

fun invalidValues1(notes: MyNotes): Int {
    val invalid = mutableListOf<Int>()
    notes.nearbyTickets.flatten().forEach { ticketValue ->
        if (notes.rules.none { it.isValid(ticketValue) }) {
            invalid.add(ticketValue)
        }
    }
    return invalid.sum()
}

data class RulesTicket(
        val field: String,
        val range1: IntRange,
        val range2: IntRange
) {
    fun isValid(value: Int): Boolean {
        return value in range1 || value in range2
    }
}

data class MyNotes(
        val rules: List<RulesTicket>,
        val myTicket: List<Int>,
        val nearbyTickets: List<List<Int>>
)

fun departureFields2(notes: MyNotes): Long {
    val validTickets = notes.nearbyTickets.filter { ticket ->
        ticket.all { ticketValue -> notes.rules.any { it.isValid(ticketValue) } }
    }.toMutableList().apply { add(notes.myTicket) } // including mine
    val rules = notes.rules.toMutableList()
    val nbIndicesTicket = validTickets.first().indices
    val correspondanceMap = mutableMapOf<String, MutableList<Int>>()
    for (r in rules) {
        for (i in nbIndicesTicket) {
            if (validTickets.all { r.isValid(it[i]) }) {
                // ca correspond
                val listIndices = correspondanceMap.getOrPut(r.field) { mutableListOf() }
                listIndices.add(i)
            }
        }
    }
    val finalCorrespondance = mutableMapOf<String, Int>()
    while (correspondanceMap.isNotEmpty()) {
        rearrangeCorrespondances(correspondanceMap, finalCorrespondance)
    }
    val indicesToTake = finalCorrespondance.filterKeys { it.startsWith("departure") }.values
    return notes.myTicket.foldIndexed(1) { index, acc, i ->
        if (indicesToTake.contains(index)) acc * i else acc
    }
}

fun rearrangeCorrespondances(correspondanceMap: MutableMap<String, MutableList<Int>>, correspMap: MutableMap<String, Int>) {
    val iterator = correspondanceMap.iterator()
    while (iterator.hasNext()) {
        val entry = iterator.next()
        if (entry.value.size == 1) {
            val index = entry.value.first()
            correspMap[entry.key] = index
            iterator.remove()
            // remove in all other lists the value index
            correspondanceMap.filterValues { it.contains(index) }.forEach { it.value.remove(index) }
        }
    }
}