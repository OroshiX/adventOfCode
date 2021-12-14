package advent

import java.util.*

fun solve14(scanner: Scanner):String {
    with (scanner) {
        val polymer = nextLine()
        nextLine()
        val rules = mutableMapOf<String,Char>()
        while(hasNext()) {
            val (s,e) = nextLine().split(" -> ")
            rules[s] = e[0]
        }
        val p = Polymer(polymer.toMutableList(), rules)
        p.step(10)
        print(p)
        return p.part1().toString()
    }    
}

data class Polymer(val polymer: MutableList<Char>, val rules: Map<String,Char>) {
    private fun step() {
        for(i in polymer.size - 1 downTo 1) {
            val toAdd = rules.getValue("${polymer[i-1]}${polymer[i]}")
            polymer.add(i, toAdd)
        }
    }
    
    fun step(n: Int) {
        for(i in 0 until n) {
            step()
        }
    }
    fun part1() : Int{
        val quantities= polymer.fold(mutableMapOf<Char,Int>()) { acc, c -> 
            acc[c] = acc.getOrDefault(c, 0) + 1
            acc
        }
        return quantities.maxOf { it.value }- quantities.minOf {it.value}
    }
}
