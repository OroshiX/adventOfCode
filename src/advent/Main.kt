package advent

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.util.*


fun main() {
    val debug = false
    val numDay = 13
    val year = 2021
    val function = ::solve13
    val startTime = System.currentTimeMillis()
    val scanner = Scanner(
        FileInputStream(
            if (debug) "D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\t"
            else saveFile(numDay, year)
        )
    )
    val res = function.invoke(scanner)
    System.err.println(
        "Took ${(System.currentTimeMillis() - startTime)} ms to execute"
    )
    print(res)
}

fun saveFile(num: Int, year: Int): String {
    val path =
        "D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\inputDay$num.txt"

    val file = File(path)
    if (file.exists())
        return path
    download(
        "https://adventofcode.com/$year/day/$num/input",
        file, System.getenv("cookie")
    )
    return path
}

fun download(link: String, file: File, cookie: String) {
    val connection = URL(link).openConnection()
    connection.setRequestProperty("Cookie", cookie)
    connection.connect()
    connection.getInputStream().use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
}