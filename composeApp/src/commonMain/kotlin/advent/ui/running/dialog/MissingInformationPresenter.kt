package advent.ui.running.dialog

import advent.ui.running.DayState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface MissingInformationPresenter {
    val uiModel: StateFlow<MissingInformationUiModel>
    fun onDebugInputChanged(input: String)
}

internal class MissingInformationPresenterImpl(private val missingInformation: DayState.MissingInformation) :
    MissingInformationPresenter {
    private val _uiModel = MutableStateFlow(
        MissingInformationUiModel(
            debug = missingInformation.debugInput,
            result = missingInformation.debugResultExpected,
            sessionCookie = missingInformation.sessionCookie,
        )
    )
    override val uiModel: StateFlow<MissingInformationUiModel> = _uiModel

    override fun onDebugInputChanged(input: String) {
        _uiModel.update { it.copy(debugInput = input) }
    }

}

data class MissingInformationUiModel(
    val debug: Boolean = false,
    val debugInput: String? = null,
    val result: Boolean = false,
    val expectedResultInput: String? = null,
    val sessionCookie: Boolean = false,
    val sessionCookieInput: String? = null
) {
    val isValid: Boolean
        get() = (!debug || debugInput.isNullOrBlank().not()) &&
                (!result || expectedResultInput.isNullOrBlank().not()) &&
                (!sessionCookie || sessionCookieInput.isNullOrBlank().not())
}