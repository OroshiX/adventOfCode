package advent.ui.running

import advent.ui.input.InputWithLabel
import advent.ui.navigation.DayRunning
import advent.ui.running.dialog.MissingInformationPresenter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import kotlin.time.DurationUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunningDayScreen(
    dayRunning: DayRunning,
    onBack: () -> Unit,
) {
    val presenter: DayPresenter = koinInject { parametersOf(dayRunning) }
    val uiState by presenter.dayState.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Running day ${uiState.dayNumber}")
        }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })
    }) {
        RunningDayContent(
            modifier = Modifier.padding(it),
            dayState = uiState,
            onStop = presenter::stop,
            onRetry = presenter::retry,
        )
    }
}

@Composable
fun RunningDayContent(
    modifier: Modifier = Modifier,
    dayState: DayState,
    onStop: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    when (dayState) {
        is DayState.DayUiModel         -> RunningDayProgress(
            modifier = modifier,
            dayState = dayState,
            onStop = onStop
        )

        is DayState.Loading            -> LoadingDay(modifier = modifier)
        is DayState.Error              -> ErrorDay(modifier = modifier, onRetry = onRetry)
        is DayState.Success            -> SuccessDay(modifier = modifier, success = dayState)
        is DayState.MissingInformation -> {
            LoadingDay(modifier = modifier)
            MissingDay(dayState, onCloseDialog = {
                TODO()
            })
        }
    }
}

@Composable
fun SuccessDay(modifier: Modifier = Modifier, success: DayState.Success) {
    Text(text = success.toString())
}

@Composable
fun MissingDay(dayState: DayState.MissingInformation, onCloseDialog: () -> Unit) {
    DialogWindow(
        onCloseRequest = onCloseDialog,
        undecorated = true,
        resizable = false,
        alwaysOnTop = true,
        state = rememberDialogState(position = WindowPosition(Alignment.Center))
    ) {
        val missingInformationPresenter: MissingInformationPresenter = koinInject {
            parametersOf(dayState)
        }

        val uiState by missingInformationPresenter.uiModel.collectAsState()

        Column {
            if (uiState.debug) {
                InputWithLabel(
                    label = "Input",
                    input = uiState.debugInput.orEmpty(),
                    onTextChanged = missingInformationPresenter::onDebugInputChanged
                )
            }
        }
    }
}


@Composable
fun ErrorDay(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Error")
        ElevatedButton(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun LoadingDay(modifier: Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator()
    }
}

@Composable
fun RunningDayProgress(dayState: DayState.DayUiModel, onStop: () -> Unit, modifier: Modifier) {
    Column(modifier = modifier) {
        LinearProgressIndicator(
            progress = {
                if (dayState.maxProgress == 0) 0f else
                    (dayState.progress.toFloat() / dayState.maxProgress)
            },
        )
        Row {
            Text(text = dayState.elapsed.toString(unit = DurationUnit.SECONDS, decimals = 3))
            IconButton(onClick = onStop) {
                Icon(imageVector = Icons.Filled.Stop, contentDescription = "Stop")
            }
        }
    }
}