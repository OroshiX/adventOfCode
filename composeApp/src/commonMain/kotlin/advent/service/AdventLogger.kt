package advent.service

import advent.ui.console.LogLine
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface AdventLogger {
    val logs: StateFlow<List<LogLine>>
    fun d(message: String)
    fun w(message: String)
    fun e(message: String)
    fun i(message: String)
    fun success(message: String)
    fun danger(message: String)
}

private val INFO = Color(0xFFC7B548)
private val DEBUG = Color(0xFF526CB3)
private val ERROR = Color(0xFF9E1111)
private val WARN = Color(0xFFBB5822)
private val SUCCESS = Color(0xFF188B18)

class AdventLoggerImpl : AdventLogger {
    private val _logs = MutableStateFlow(emptyList<LogLine>())
    override val logs: StateFlow<List<LogLine>> = _logs

    override fun i(message: String) {
        _logs.update { it + LogLine(message, color = INFO) }
    }

    override fun e(message: String) {
        _logs.update { it + LogLine(message, ERROR) }
    }

    override fun w(message: String) {
        _logs.update { it + LogLine(message, WARN) }
    }

    override fun d(message: String) {
        _logs.update { it + LogLine(message, DEBUG) }
    }

    override fun success(message: String) {
        _logs.update { it + LogLine(message, SUCCESS) }
    }

    override fun danger(message: String) {
        e(message)
    }
}