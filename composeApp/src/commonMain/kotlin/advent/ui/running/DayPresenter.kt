package advent.ui.running

import advent.Part
import advent.service.AdventSolver
import advent.ui.config.ConfigManipulator
import advent.ui.config.FileSaver
import advent.ui.navigation.DayRunning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration

interface DayPresenter {
    val dayState: StateFlow<DayState>
    fun retry()
    fun stop()
}

class DayPresenterImpl(
    private val configManipulator: ConfigManipulator,
    private val dayRunning: DayRunning,
    private val adventSolver: AdventSolver,
) : DayPresenter {
    private val _dayState: MutableStateFlow<DayState> =
        MutableStateFlow(DayState.Loading(dayRunning.dayNumber))
    override val dayState: StateFlow<DayState> = _dayState
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
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


    private suspend fun startSolving() {
        _dayState.emit(DayState.Loading(dayRunning.dayNumber))
        val part = Part.fromNumber(dayRunning.part)
            ?: run {
                _dayState.update { DayState.Error(it.dayNumber) }
                return
            }

        val result = adventSolver.solve(
            dayRunning.dayNumber,
            part = part,
            debug = dayRunning.debug,
            file = FileSaver.inputFile(
                numDay = dayRunning.dayNumber,
                part = part,
                debug = dayRunning.debug
            ),
            onProgressUpdate = { progress ->
                _dayState.update {
                    (it as? DayState.DayUiModel)?.copy(
                        progress = progress.current,
                        maxProgress = progress.max,
                    ) ?: it
                }
            },
            onUpdateElapsed = { duration ->
                _dayState.update { (it as? DayState.DayUiModel)?.copy(elapsed = duration) ?: it }
            }
        )

        when {
            result.isSuccess -> {
                val (res, elapsedTime) = result.getOrThrow()
                _dayState.update {
                    DayState.Success(
                        dayNumber = it.dayNumber,
                        elapsed = elapsedTime,
                        result = res
                    )
                }
            }

            result.isFailure -> {
                _dayState.update { DayState.Error(dayNumber = it.dayNumber) }
            }
        }
    }

    override fun retry() {
        coroutineScope.launch {
            startSolving()
        }
    }

    override fun stop() {
        coroutineScope.cancel(message = "User cancelled")
        _dayState.update { DayState.Error(it.dayNumber) }
    }
}
