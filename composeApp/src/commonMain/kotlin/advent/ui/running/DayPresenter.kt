package advent.ui.running

import advent.Part
import advent.ui.config.ConfigManipulator
import advent.ui.config.FileSaver
import advent.ui.navigation.DayRunning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration

interface DayPresenter {
    val dayState: StateFlow<DayState>
    fun retry()
}

class DayPresenterImpl(
    private val configManipulator: ConfigManipulator,
    private val dayRunning: DayRunning,
) : DayPresenter {
    private val _dayState: MutableStateFlow<DayState> =
        MutableStateFlow(DayState.Loading(dayRunning.dayNumber))
    override val dayState: StateFlow<DayState> = _dayState

    init {
        CoroutineScope(Dispatchers.IO).launch {
            checkInputs()
        }
    }

    private suspend fun checkInputs() {
        val numDay = dayRunning.dayNumber
        val part = Part.fromNumber(dayRunning.part) ?: run {
            _dayState.update { DayState.Error(it.dayNumber) }
            return
        }
        val year = dayRunning.year
        val debug = dayRunning.debug

        val cookie = configManipulator.sessionCookie
        withContext(Dispatchers.IO) {
            if (debug) {
                val missingInput = FileSaver.debugExists(numDay, part).not()
                val missingResult =
                    configManipulator.getExpectedResult(numDay, part).isNullOrBlank()
                _dayState.update {
                    if (missingResult || missingInput) {
                        DayState.MissingInformation(
                            dayNumber = it.dayNumber,
                            debugInput = missingInput,
                            debugResultExpected = missingResult,
                        )
                    } else {
                        DayState.DayUiModel(
                            dayNumber = numDay,
                            progress = 0,
                            maxProgress = 1,
                            elapsed = Duration.ZERO,
                        )
                    }
                }
            } else {
                // real data
                val realFile = FileSaver.realData(numDay, year, cookie)
                when {
                    realFile.isFailure -> _dayState.update {
                        DayState.MissingInformation(
                            dayNumber = it.dayNumber,
                            sessionCookie = true,
                            additionalReason = realFile.exceptionOrNull()?.message,
                        )
                    }

                    realFile.isSuccess -> _dayState.update {
                        DayState.DayUiModel(
                            dayNumber = numDay,
                            progress = 0,
                            maxProgress = 1,
                            elapsed = Duration.ZERO,
                        )
                    }
                }
            }
        }
    }

    private fun startSolving() {
        // TODO
    }

    override fun retry() {
        _dayState.update { DayState.Loading(it.dayNumber) }
        TODO("Not yet implemented")
    }
}
