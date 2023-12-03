package advent.days

import advent.DayPuzzle
import java.util.*

class Day2 : DayPuzzle<List<GameDay2>>() {
    override fun parse(scanner: Scanner): List<GameDay2> {
        val games = mutableListOf<GameDay2>()
        val regexBlue = Regex("""(\d+) blue""")
        val regexRed = Regex("""(\d+) red""")
        val regexGreen = Regex("""(\d+) green""")
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val (game, rest) = line.split(": ")
            val id = game.substringAfter("Game ").toInt()
            val roundsString = rest.split("; ")
            val rounds = mutableListOf<RoundDay2>()
            roundsString.forEach { round ->
                var (blue, red, green) = Triple(0, 0, 0)
                round.split(", ").forEach { cube ->
                    if (regexBlue.matches(cube))
                        blue = regexBlue.find(cube)!!.groupValues[1].toInt()
                    else if (regexRed.matches(cube))
                        red = regexRed.find(cube)!!.groupValues[1].toInt()
                    else if (regexGreen.matches(cube))
                        green = regexGreen.find(cube)!!.groupValues[1].toInt()
                }
                rounds.add(RoundDay2(blue, red, green))
            }
            games.add(GameDay2(id, rounds))
        }
        return games
    }

    override fun solve1(input: List<GameDay2>): String {
        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14
        var sumIdsPossible = 0
        for (game in input) {
            if (game.maxRed <= maxRed && game.maxGreen <= maxGreen && game.maxBlue <= maxBlue) {
                sumIdsPossible += game.id
            }
        }
        return sumIdsPossible.toString()
    }

    override fun solve2(input: List<GameDay2>): String {
        var sum = 0
        for (game in input) {
            val power = game.maxRed * game.maxBlue * game.maxGreen
            sum += power
        }
        return sum.toString()
    }

}

data class GameDay2(val id: Int, val rounds: List<RoundDay2>) {
    val maxRed: Int
        get() = rounds.maxOf { it.red }
    val maxGreen: Int
        get() = rounds.maxOf { it.green }
    val maxBlue: Int
        get() = rounds.maxOf { it.blue }
}

data class RoundDay2(val blue: Int, val red: Int, val green: Int)
