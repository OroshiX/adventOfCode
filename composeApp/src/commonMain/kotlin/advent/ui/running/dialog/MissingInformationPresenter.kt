package advent.ui.running.dialog

import advent.Part
import advent.ui.config.ConfigManipulator
import advent.ui.config.FileSaver
import advent.ui.running.DayState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface MissingInformationPresenter {
    val uiModel: StateFlow<MissingInformationUiModel>
    fun onDebugInputChanged(input: String)
    fun onExpectedResultInputChanged(input: String)
    fun onSessionCookieInputChanged(input: String)
    fun validate()
}

internal class MissingInformationPresenterImpl(
    dayNumber: Int,
    private val missingInformation: DayState.MissingInformation,
    private val configManipulator: ConfigManipulator
) :
    MissingInformationPresenter {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val _uiModel = MutableStateFlow(
        MissingInformationUiModel(
            dayNumber = dayNumber,
            debug = missingInformation.debugInput,
            result = missingInformation.debugResultExpected,
            sessionCookie = missingInformation.sessionCookie,
            part = missingInformation.part,
            year = missingInformation.year,
        )
    )
    override val uiModel: StateFlow<MissingInformationUiModel> = _uiModel

    override fun onDebugInputChanged(input: String) {
        _uiModel.update { it.copy(debugInput = input) }
    }

    override fun onExpectedResultInputChanged(input: String) {
        _uiModel.update { it.copy(expectedResultInput = input) }
    }

    override fun onSessionCookieInputChanged(input: String) {
        _uiModel.update { it.copy(sessionCookieInput = input) }
    }

    override fun validate() {
        val value = _uiModel.value
        _uiModel.update { it.copy(state = MissingState.Processing) }
        // TODO: update in files
        coroutineScope.launch {
            var res = true
            if (value.debug) {
                val writeDebugFile = FileSaver.writeDebugFile(
                    value.dayNumber,
                    part = value.part,
                    content = value.debugInput.orEmpty()
                )
                res = writeDebugFile.isSuccess
            }

            if (value.result) {
                configManipulator.setExpectedResult(
                    value.dayNumber,
                    value.part,
                    value.expectedResultInput.orEmpty()
                )
            }

            if (value.sessionCookie) {
                configManipulator.sessionCookie = value.sessionCookieInput.orEmpty()

                val realData = FileSaver.realData(
                    numDay = value.dayNumber,
                    year = value.year,
                    cookie = value.sessionCookieInput
                )
                res = res && realData.isSuccess
            }
            _uiModel.update { it.copy(state = if(res) MissingState.Done else MissingState.Error) }
        }
    }
}

data class MissingInformationUiModel(
    val dayNumber: Int,
    val debug: Boolean = false,
    val debugInput: String? = null,
    val result: Boolean = false,
    val expectedResultInput: String? = null,
    val sessionCookie: Boolean = false,
    val sessionCookieInput: String? = null,
    val part: Part,
    val doneInputting: Boolean = false,
    val state: MissingState = MissingState.Input,
    val year: Int,
) {
    val isValid: Boolean
        get() = (!debug || debugInput.isNullOrBlank().not()) &&
                (!result || expectedResultInput.isNullOrBlank().not()) &&
                (!sessionCookie || sessionCookieInput.isNullOrBlank().not())
}

enum class MissingState {
    Input,
    Processing,
    Done,
    Error
}