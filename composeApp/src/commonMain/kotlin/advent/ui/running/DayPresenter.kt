package advent.ui.running

import advent.ui.config.ConfigManipulator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

interface DayPresenter {
    val dayState: StateFlow<DayUiModel>
}

class DayPresenterImpl(private val configManipulator: ConfigManipulator) : DayPresenter {
    private val _dayState = MutableStateFlow(
        DayUiModel(
            dayNumber = configManipulator.numDay,
            progress = 0,
            maxProgress = 1,
            elapsed = Duration.ZERO,
        )
    )
    override val dayState: StateFlow<DayUiModel> = _dayState


}
