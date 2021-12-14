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
        val p = Polymer(polymer, rules)
        p.step(10)
        print(p)
        return p.part1().toString()
    }    
}

data class Polymer(var polymer: String, val rules: Map<String,Char>) {
    private fun step() {
        val toAdd = mutableListOf<Char>()
        for(i in 1 until polymer.length) {
            toAdd+= rules.getValue(polymer.substring(i-1..i))
        }
        val sb = StringBuilder()
        for(i in 0 until polymer.length) {
            sb.append(polymer[i])
            if(i < polymer.length -1) {
            	sb.append(toAdd[i])
            }
        }
        polymer= sb.toString()
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
