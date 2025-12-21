package advent.ui.running

import advent.Part
import kotlin.time.Duration

sealed class DayState {

    data class DayUiModel(
        val progress: Int,
        val maxProgress: Int,
    ) : DayState()

    data object Loading : DayState()
    data class Error(val message: String) : DayState()
    data class MissingInformation(
        val debugInput: Boolean = false,
        val debugResultExpected: Boolean = false,
        val sessionCookie: Boolean = false,
        val additionalReason: String? = null,
        val part: Part,
        val year: Int,
    ) : DayState()

    data class Success(
        val elapsed: Duration,
        val result: String,
    ) : DayState()
}