package advent

import java.util.*

fun solve23(scanner: Scanner): String {
    with(scanner) {
        val list = nextLine().map { Character.getNumericValue(it) }
        val linkedList = DoubleLinkedList.createFromList(list)
        System.err.println(linkedList.printableValue())
        for (i in 0 until 100) {
            linkedList.step()
            System.err.println(linkedList.printableValue())
        }
        return linkedList.printableValue()
    }
}

class DoubleLinkedList(var head: LinkedNode) {

    data class LinkedNode(val value: Int, var next: LinkedNode? = null) {
        override fun toString(): String {
            return "($value)"
        }
    }

    companion object {
        fun createFromList(list: List<Int>): DoubleLinkedList {
            val map = list.map { it to LinkedNode(it) }.toMap()
            map.getValue(list.last()).next = map.getValue(list.first())
            for (i in 0 until list.size - 1) {
                map.getValue(list[i]).next = map.getValue(list[i + 1])
            }
            return DoubleLinkedList(map.getValue(list.first()))
        }
    }

    fun printableValue(): String {
        var currentNode = head
        while (currentNode.value != 1) {
            currentNode = currentNode.next ?: throw IllegalArgumentException()
        }
        currentNode = currentNode.next ?: throw IllegalArgumentException()
        val sb = StringBuilder()
        while (currentNode.value != 1) {
            sb.append(currentNode.value)
            currentNode = currentNode.next!!
        }
        return sb.toString()
    }

    fun step() { // pick 3 cups to remove
        val picked = head.next ?: throw IllegalArgumentException()
        val lastPicked = picked.next!!.next!!
        val setPicked =
            setOf(picked.value, picked.next!!.value, picked.next!!.next!!.value)

        // destination cup
        var destinationLabel = if (head.value > 1) head.value - 1 else 9
        while (setPicked.contains(
                destinationLabel
            )) { // select a new destination
            destinationLabel =
                if (destinationLabel > 1) destinationLabel - 1 else 9
        }

        // put the picked cups at the after the destination cup
        // look for destination node
        var destinationNode = lastPicked.next!!
        while (destinationNode.value != destinationLabel) {
            destinationNode = destinationNode.next!!
        }

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