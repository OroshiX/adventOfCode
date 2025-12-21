package advent.ui.input

import advent.Part
import advent.ui.config.ConfigManipulator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface AskInputPresenter {
    val inputState: StateFlow<InputUiModel>
    fun changeDay(dayNumber: Int)
    fun changePart(part: Part)
    fun changeData(isDebug: Boolean)
    fun validate()
}

internal class AskInputPresenterImpl(val configManipulator: ConfigManipulator) : AskInputPresenter {
    private val _inputState = MutableStateFlow(
        InputUiModel(
            dayNumber = configManipulator.numDay,
            part = configManipulator.part,
            isDebug = configManipulator.debug,
        )
    )
    override val inputState: StateFlow<InputUiModel> = _inputState
    override fun changeDay(dayNumber: Int) {
        configManipulator.numDay = dayNumber
        _inputState.update {
            it.copy(dayNumber = dayNumber)
        }
    }

    override fun changePart(part: Part) {
        configManipulator.part = part
        _inputState.update { it.copy(part = part) }
    }

    override fun changeData(isDebug: Boolean) {
        configManipulator.debug = isDebug
        _inputState.update { it.copy(isDebug = isDebug) }
    }

    override fun validate() {
        val uiModel = inputState.value
        configManipulator.apply {
            debug = uiModel.isDebug
            numDay = uiModel.dayNumber
            part = uiModel.part
        }
    }
}