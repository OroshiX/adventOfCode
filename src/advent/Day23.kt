package advent

import java.util.*

const val STEP1 = 100
const val STEP2 = 10_000_000
const val MAX_1 = 9
const val MAX_2 = 1_000_000
fun solve23(scanner: Scanner): String {
    with(scanner) {
        val list = parseCrabInput2(nextLine())
        val (map, linkedList) = DoubleLinkedList.createFromList(list)
//        System.err.println(linkedList.printableValue(map))
        for (i in 0 until STEP2) {
            linkedList.step(map, MAX_2)
        }
//        System.err.println(linkedList.printableValue(map))
        return linkedList.printableValue2(map)
    }
}

fun parseCrabInput1(line: String): List<Int> {
    return line.map { Character.getNumericValue(it) }
}

fun parseCrabInput2(line: String): List<Int> {
    return line.map { Character.getNumericValue(it) }.toMutableList()
        .apply { addAll(10..1_000_000) }
}

class DoubleLinkedList(private var head: LinkedNode) {

    data class LinkedNode(val value: Int, var next: LinkedNode? = null) {
        override fun toString(): String {
            return "($value)"
        }
    }

    companion object {
        fun createFromList(
            list: List<Int>): Pair<Map<Int, LinkedNode>, DoubleLinkedList> {
            val map = list.map { it to LinkedNode(it) }.toMap()
            map.getValue(list.last()).next = map.getValue(list.first())
            for (i in 0 until list.size - 1) {
                map.getValue(list[i]).next = map.getValue(list[i + 1])
            }
            return map to DoubleLinkedList(map.getValue(list.first()))
        }
    }

    fun printableValue(map: Map<Int, LinkedNode>): String {
        var currentNode = map[1]!!.next!!
        val sb = StringBuilder()
        while (currentNode.value != 1) {
            sb.append(currentNode.value)
            currentNode = currentNode.next!!
        }
        return sb.toString()
    }

    fun printableValue2(map: Map<Int, LinkedNode>): String {
        val currentNode = map[1]!!.next!!
        return (currentNode.value.toLong() * currentNode.next!!.value.toLong()).toString()
    }

    fun step(map: Map<Int, LinkedNode>, maxLabel: Int) {

        // pick 3 cups to remove
        val picked = head.next ?: throw IllegalArgumentException()
        val lastPicked = picked.next!!.next!!
        val setPicked =
            setOf(picked.value, picked.next!!.value, picked.next!!.next!!.value)

        // destination cup
        var destinationLabel = if (head.value > 1) head.value - 1 else maxLabel
        while (setPicked.contains(
                destinationLabel
            )) { // select a new destination
            destinationLabel =
                if (destinationLabel > 1) destinationLabel - 1 else maxLabel
        }

        // put the picked cups at the after the destination cup
        // look for destination node
        val destinationNode = map[destinationLabel]!!

        // put the picked cups at the after the destination cup
        head.next = lastPicked.next
        lastPicked.next = destinationNode.next
        destinationNode.next = picked

        // new current cup
        head = head.next!!
    }

    override fun toString(): String {
        return "$head"
    }
}