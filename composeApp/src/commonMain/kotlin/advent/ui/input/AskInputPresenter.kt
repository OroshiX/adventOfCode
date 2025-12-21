package advent.ui.input

import advent.Part
import advent.ui.config.ConfigManipulator
import advent.ui.navigation.DayRunning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface AskInputPresenter {
    val inputState: StateFlow<InputUiModel>
    fun changeDay(dayNumber: Int)
    fun changePart(part: Part)
    fun changeYear(year: Int)
    fun changeData(isDebug: Boolean)
    fun validate(): DayRunning
}

internal class AskInputPresenterImpl(val configManipulator: ConfigManipulator) : AskInputPresenter {
    private val _inputState = MutableStateFlow(
        InputUiModel(
            dayNumber = configManipulator.numDay,
            part = configManipulator.part,
            isDebug = configManipulator.debug,
            year = configManipulator.year,
        )
    )
    override val inputState: StateFlow<InputUiModel> = _inputState
    override fun changeDay(dayNumber: Int) {
        configManipulator.numDay = dayNumber
        _inputState.update {
            it.copy(dayNumber = dayNumber)
        }
    }

    override fun changeYear(year: Int) {
        configManipulator.year = year
        _inputState.update { it.copy(year = year) }
    }

    override fun changePart(part: Part) {
        configManipulator.part = part
        _inputState.update { it.copy(part = part) }
    }

    override fun changeData(isDebug: Boolean) {
        configManipulator.debug = isDebug
        _inputState.update { it.copy(isDebug = isDebug) }
    }

    override fun validate(): DayRunning {
        val uiModel = inputState.value
        configManipulator.apply {
            debug = uiModel.isDebug
            numDay = uiModel.dayNumber
            part = uiModel.part
        }
        return DayRunning(
            dayNumber = uiModel.dayNumber,
            part = uiModel.part.number,
            debug = uiModel.isDebug,
            year = uiModel.year,
        )
    }
}