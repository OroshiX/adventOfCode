package advent

import java.util.*

fun solve19(scanner: Scanner): String {
    with(scanner) {
        var line = nextLine()
        val rules = mutableMapOf<Int, ContentRule>() // read rules
        while (line.isNotEmpty()) {
            val ruleMessage = treatRuleMessageInput(line)
            rules[ruleMessage.first] = ruleMessage.second
            line = nextLine()
        }

        val messages = mutableListOf<String>()
        while (hasNext()) {
            line = nextLine()
            messages.add(line)
        }

        val possibles = generateAllValidMessages(rules)
        return messages.filter { possibles.contains(it) }.size.toString()
    }
}

fun generateAllValidMessages(rules: Map<Int, ContentRule>): List<String> {
    TODO("Not yet implemented")
}

fun treatRuleMessageInput(line: String): Pair<Int, ContentRule> {
    val list = line.split(": ", " | ")
    val number = list.first().toInt()
    when (list.size) {
        3 -> {
            val firstPart = list[1].split(" ").map { it.toInt() }
            val secondPart = list[2].split(" ").map { it.toInt() }
            return number to ChoiceRule(firstPart, secondPart)
        }
        2 -> {
            return if (list[1].startsWith("\"") && list[1].length == 3) {
                number to Leaf(list[1][1])
            } else {
                number to SequenceContent(list[1].split(" ").map { it.toInt() })
            }
        }
        else -> throw IllegalArgumentException(
            "list size ${list.size} not legal"
        )
    }
}

sealed class ContentRule
class ChoiceRule(val first: List<Int>, val second: List<Int>) : ContentRule()
class Leaf(val char: Char) : ContentRule()
data class SequenceContent(val rules: List<Int>) : ContentRule()