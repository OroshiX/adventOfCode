package advent.ui.console

import advent.service.AdventLogger
import kotlinx.coroutines.flow.StateFlow

interface TerminalPresenter {
    val logLines: StateFlow<List<LogLine>>
    fun clear()
}

class TerminalPresenterImpl(private val logger: AdventLogger) : TerminalPresenter {
    override val logLines: StateFlow<List<LogLine>> = logger.logs
    override fun clear() {
        logger.clearLogs()
    }
}