package advent.ui.running

import kotlin.time.Duration

data class DayUiModel(
    val dayNumber: Int,
    val progress: Int,
    val maxProgress: Int,
    val elapsed: Duration
)
