package advent.ui.config

import advent.Part
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Paths

object FileSaver {
    private val directory = File(Paths.get("").toAbsolutePath().toString(), "inputs").apply {
        if (!exists()) mkdir()
    }

    fun realData(numDay: Int, year: Int, cookie: String?): Result<File> {
        val file = File(directory, "input$numDay.txt")
        if (file.exists()) return Result.success(file)
        if (cookie == null) return Result.failure(MissingCookieException())
        try {
            download(
                "https://adventofcode.com/$year/day/$numDay/input",
                file, "session=$cookie"
            )
        } catch (e: Throwable) {
            return Result.failure(e)
        }
        return Result.success(file)
    }

    private fun debugFilename(numDay: Int, part: Part) = "t$numDay-${part.key}"
    fun debugExists(numDay: Int, part: Part): Boolean {
        return File(directory, debugFilename(numDay, part)).exists()
    }

    fun fileDebug(numDay: Int, part: Part): File {
        val file = File(directory, debugFilename(numDay, part))
        return file
    }

    fun writeDebugFile(numDay: Int, part: Part, content: String): Result<Unit> {
        val file = File(directory, debugFilename(numDay, part))
        if (!file.exists()) {
            file.createNewFile()
        }
        try {
            FileOutputStream(file, true).use {
                it.write(content.toByteArray(Charsets.UTF_8))
                return Result.success(Unit)
            }
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }

    private fun download(link: String, file: File, cookie: String) {
        val connection = URL(link).openConnection()
        connection.setRequestProperty("Cookie", cookie)
        connection.connect()
        connection.getInputStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }
}

class MissingCookieException() :
    Exception("No cookie set when trying to download from advent of code")