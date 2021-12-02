package advent

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.util.*


fun main(argv: Array<String>) {
    val debug = false
    val numDay = 25
    val year = 2021
    val function = ::solve25
    val startTime = System.currentTimeMillis()
    val scanner = Scanner(
        FileInputStream(
            if (debug) "D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\t"
            else saveFile(numDay, year)
        )
    )
    val res = function.invoke(scanner)
    System.err.println(
        "Took ${(System.currentTimeMillis() - startTime) / 1000} s to execute"
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
        file,
        "_ga=GA1.2.433693440.1607594672; session=53616c7465645f5feede30b44515d84eff1fa01590e28e7a852092c6af3f5565c4a0d0502f843d5d054fa19a153919a1; _gid=GA1.2.1812765835.1607875133"
    )
    // session=53616c7465645f5fa1a996be127cbd99cd1dc27cbc3d31c0dd516465e205c59eba292ccd6fdc6756f67bd0809a010c1d
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