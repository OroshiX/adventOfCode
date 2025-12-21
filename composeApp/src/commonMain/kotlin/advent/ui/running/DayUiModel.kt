package advent.ui.running

import kotlin.time.Duration

sealed class DayState {
    abstract val dayNumber: Int

    data class DayUiModel(
        override val dayNumber: Int,
        val progress: Int,
        val maxProgress: Int,
        val elapsed: Duration
    ) : DayState()

    data class Loading(override val dayNumber: Int) : DayState()
    data class Error(override val dayNumber: Int) : DayState()
    data class MissingInformation(
        override val dayNumber: Int,
        val debugInput: Boolean = false,
        val debugResultExpected: Boolean = false,
        val sessionCookie: Boolean = false,
        val additionalReason: String? = null,
    ) : DayState()

    data class Success(
        override val dayNumber: Int,
        val elapsed: Duration,
        val result: String,
    ) : DayState()
}