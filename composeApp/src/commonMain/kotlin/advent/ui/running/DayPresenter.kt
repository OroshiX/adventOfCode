package advent.ui.running

import advent.Part
import advent.service.AdventSolver
import advent.ui.config.ConfigManipulator
import advent.ui.config.FileSaver
import advent.ui.navigation.DayRunning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration

interface DayPresenter {
    val dayState: StateFlow<DayState>
    val durationState: StateFlow<Duration?>
    val dayNumber: Int
    fun retry()
    fun stop()
    fun closeDialogMissing()
}

class DayPresenterImpl(
    private val configManipulator: ConfigManipulator,
    private val dayRunning: DayRunning,
    private val adventSolver: AdventSolver,
) : DayPresenter {
    private val _durationState: MutableStateFlow<Duration?> = MutableStateFlow(null)
    override val durationState: StateFlow<Duration?> = _durationState
    private val _dayState: MutableStateFlow<DayState> = MutableStateFlow(DayState.Loading)
    override val dayNumber: Int = dayRunning.dayNumber
    override val dayState: StateFlow<DayState> = _dayState
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null

    init {
        coroutineScope.launch {
            checkInputs()
        }
    }

    private suspend fun checkInputs() {
        val numDay = dayRunning.dayNumber
        val part = Part.fromNumber(dayRunning.part) ?: run {
            _dayState.update { DayState.Error("Part ${dayRunning.part} does not exist") }
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
                if (missingResult || missingInput) {
                    _dayState.update {
                        DayState.MissingInformation(
                            debugInput = missingInput,
                            debugResultExpected = missingResult,
                            part = part,
                            year = year,
                        )
                    }
                } else {
                    _dayState.update {
                        DayState.DayUiModel(
                            progress = 0,
                            maxProgress = 1,
                        )
                    }
                    _durationState.update { Duration.ZERO }
                    job = coroutineScope.launch { startSolving() }
                }

            } else {
                // real data
                val realFile = FileSaver.realData(numDay, year, cookie)
                when {
                    realFile.isFailure -> _dayState.update {
                        DayState.MissingInformation(
                            sessionCookie = true,
                            additionalReason = realFile.exceptionOrNull()?.message,
                            part = part,
                            year = year,
                        )
                    }

                    realFile.isSuccess -> {
                        _dayState.update {
                            DayState.DayUiModel(
                                progress = 0,
                                maxProgress = 1,
                            )
                        }
                        _durationState.update { Duration.ZERO }
                        job = coroutineScope.launch { startSolving() }
                    }
                }
            }
        }
    }


    private suspend fun startSolving() {
        _dayState.emit(DayState.Loading)
        val part = Part.fromNumber(dayRunning.part)
            ?: run {
                _dayState.update { DayState.Error("Part ${dayRunning.part} does not exist") }
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
                    DayState.DayUiModel(progress = progress.current, maxProgress = progress.max)
                }
            },
            onUpdateElapsed = { duration ->
                _durationState.update { duration }
            }
        )

        when {
            result.isSuccess -> {
                val (res, elapsedTime) = result.getOrThrow()
                _dayState.update {
                    DayState.Success(
                        elapsed = elapsedTime,
                        result = res
                    )
                }
            }

            result.isFailure -> {
                _dayState.update {
                    DayState.Error(
                        "Failed to solve with error: ${result.exceptionOrNull()?.message}\n${result.exceptionOrNull()}\n${
                            result.exceptionOrNull()?.stackTraceToString()
                        }"
                    )
                }
            }
        }
    }

    override fun retry() {
        job?.cancel()
        job = coroutineScope.launch {
            startSolving()
        }
    }

    override fun closeDialogMissing() {
        job = coroutineScope.launch { startSolving() }
    }

    override fun stop() {
        job?.cancel("User cancelled")
        _dayState.update { DayState.Error("You cancelled the problem solving") }
    }
}
