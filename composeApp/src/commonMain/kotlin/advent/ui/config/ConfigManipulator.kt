package advent.ui.config

import advent.Part
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Paths
import java.util.Properties

interface ConfigManipulator {
    var numDay: Int
    var part: Part
    var debug: Boolean
    var sessionCookie: String?
    fun getExpectedResult(numDay: Int, part: Part): String?
    fun setExpectedResult(numDay: Int, part: Part, expected: String)
}

private const val CONFIG = "config.properties"
private const val KEY_NUM_DAY = "numDay"
private const val KEY_PART = "part"
private const val KEY_DEBUG = "debug"
private const val KEY_SESSION = "session"

internal class ConfigManipulatorImpl : ConfigManipulator {
    private val directory = File(Paths.get("").toAbsolutePath().toString(), "inputs").apply {
        if (!exists()) mkdir()
    }
    private val file: File = File(directory, CONFIG)

    private val props = Properties()

    init {
        if (file.exists()) {
            FileInputStream(file).use {
                props.load(it)
            }
        } else {
            file.createNewFile()
        }
    }

    override var numDay: Int
        get() = getProperty(KEY_NUM_DAY)?.toIntOrNull() ?: 1
        set(value) {
            setProperty(KEY_NUM_DAY, value.toString())
        }
    override var part: Part
        get() = getProperty(KEY_PART)?.toIntOrNull()?.let { Part.entries[it - 1] } ?: Part.ONE
        set(value) {
            setProperty(KEY_PART, value.number.toString())
        }

    override var debug: Boolean
        get() = getProperty(KEY_DEBUG)?.toBoolean() ?: true
        set(value) {
            setProperty(KEY_DEBUG, value.toString())
        }

    override var sessionCookie: String?
        get() = getProperty(KEY_SESSION).takeIf { it.isNullOrBlank().not() }
        set(value) {
            setProperty(KEY_SESSION, value.orEmpty())
        }

    private fun expectedKey(numDay: Int, part: Part) = "expected.$numDay.${part.key}"

    override fun getExpectedResult(numDay: Int, part: Part): String? {
        return getProperty(expectedKey(numDay, part))
    }

    override fun setExpectedResult(numDay: Int, part: Part, expected: String) {
        setProperty(expectedKey(numDay, part), expected)
    }

    private fun getProperty(key: String): String? = props.getProperty(key)
    private fun setProperty(key: String, value: String) {
        props.setProperty(key, value)
        FileOutputStream(file).use {
            props.store(it, "Advent of Code Config")
        }
    }
}