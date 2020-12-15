package advent

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.util.*


fun main(args: Array<String>) {
    val debug: Boolean = false
    val numDay = 14
    val function = ::solve14
    val startTime = System.currentTimeMillis()
    val scanner =
        Scanner(
            FileInputStream(
                if (debug) "D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\t"
                else saveFile(numDay)
            )
        )
    val res = function.invoke(scanner)
    System.err.println("Took ${(System.currentTimeMillis() - startTime) / 1000} s to execute")
    print(res)
}

fun saveFile(num: Int): String {
    val path =
        "D:\\Documents\\Dev\\projects\\adventOfCode\\inputs\\inputDay$num.txt"

    val file = File(path)
    if (file.exists())
        return path
    download(
        "https://adventofcode.com/2020/day/$num/input",
        file,
        "_ga=GA1.2.433693440.1607594672; session=53616c7465645f5feede30b44515d84eff1fa01590e28e7a852092c6af3f5565c4a0d0502f843d5d054fa19a153919a1; _gid=GA1.2.1812765835.1607875133"
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