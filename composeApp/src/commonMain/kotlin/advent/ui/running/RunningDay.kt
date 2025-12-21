package advent.ui.running

import advent.ui.console.TerminalLog
import advent.ui.console.TerminalPresenter
import advent.ui.input.InputWithLabel
import advent.ui.navigation.DayRunning
import advent.ui.running.dialog.MissingInformationPresenter
import advent.ui.running.dialog.MissingState
import advent.ui.theme.AdventTheme
import advent.ui.theme.codeStyle
import advent.ui.theme.consoleContainer
import advent.ui.theme.onConsoleContainer
import adventofcode.composeapp.generated.resources.Res
import adventofcode.composeapp.generated.resources.ic_input
import adventofcode.composeapp.generated.resources.ic_terminal
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import com.github.ajalt.mordant.rendering.TextColors
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import kotlin.time.Duration
import kotlin.time.DurationUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunningDayScreen(
    dayRunning: DayRunning,
    onBack: () -> Unit,
) {
    val presenter: DayPresenter = koinInject { parametersOf(dayRunning) }
    val uiState by presenter.dayState.collectAsState()
    val duration by presenter.durationState.collectAsState()
    var showInput by remember { mutableStateOf(false) }
    var showTerminal by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(true) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Running day ${presenter.dayNumber}")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }, bottomBar = {
            Row {
                IconButton(onClick = {
                    showInput = !showInput
                }) {
                    Icon(
                        painterResource(Res.drawable.ic_input),
                        contentDescription = "Show input",
                        tint = if (showInput) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    )
                }
                IconButton(onClick = {
                    showTerminal = !showTerminal
                }) {
                    Icon(
                        painterResource(Res.drawable.ic_terminal),
                        tint = if (showTerminal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        contentDescription = "Show terminal"
                    )
                }
                IconButton(onClick = {
                    showResult = !showResult
                }) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Show results",
                        tint = if (showResult) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }) {
        Column(modifier = Modifier.padding(it).consumeWindowInsets(it)) {
            AnimatedVisibility(visible = showResult) {
                RunningDayContent(
                    dayState = uiState,
                    durationState = duration,
                    dayNumber = dayRunning.dayNumber,
                    onStop = presenter::stop,
                    onRetry = presenter::retry,
                    onMissingInformationComplete = presenter::closeDialogMissing,
                    onBack = onBack,
                )
            }
            AnimatedVisibility(visible = showTerminal) {
                val presenter: TerminalPresenter = koinInject()

                val logs by presenter.logLines.collectAsState()
                TerminalLog(logsLines = logs, onClear = presenter::clear)
            }
            AnimatedVisibility(visible = showInput) {
                // TODO show input here
                Text("Your input was: ")
            }
        }
    }
}

@Composable
fun RunningDayContent(
    dayNumber: Int,
    dayState: DayState,
    onStop: () -> Unit = {},
    onRetry: () -> Unit = {},
    onBack: () -> Unit = {},
    onMissingInformationComplete: () -> Unit = {},
    durationState: Duration?,
) {
    when (dayState) {
        is DayState.DayUiModel -> RunningDayProgress(
            dayState = dayState,
            onStop = onStop,
            durationState = durationState,
        )

        is DayState.Loading -> LoadingDay()
        is DayState.Error -> ErrorDay(
            onRetry = onRetry,
            message = dayState.message
        )

        is DayState.Success -> SuccessDay(success = dayState)
        is DayState.MissingInformation -> {
            LoadingDay()
            MissingDay(dayNumber = dayNumber, dayState = dayState, onCancel = {
                onBack()
            }, onMissingInformationComplete = onMissingInformationComplete)
        }
    }
}

@Composable
fun SuccessDay(modifier: Modifier = Modifier, success: DayState.Success) {
    var widthCol1 by remember { mutableStateOf(0) }
    val width = with(LocalDensity.current) {
        widthCol1.toDp()
    }
    Column(
        modifier = modifier.padding(vertical = 16.dp, horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Result: ",
                modifier = Modifier.sizeIn(minWidth = width)
                    .onGloballyPositioned {
                        if (it.size.width > widthCol1) {
                            widthCol1 = it.size.width
                        }
                    },
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.consoleContainer()),
                text = success.result,
                color = MaterialTheme.colorScheme.onConsoleContainer(),
                style = MaterialTheme.typography.titleMedium.codeStyle()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Found in: ",
                modifier = Modifier
                    .widthIn(min = width)
                    .onGloballyPositioned {
                        if (widthCol1 < it.size.width) {
                            widthCol1 = it.size.width
                        }
                    },
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = success.elapsed.toString(DurationUnit.MILLISECONDS),
                modifier = Modifier.background(color = MaterialTheme.colorScheme.consoleContainer()),
                color = MaterialTheme.colorScheme.onConsoleContainer(),
                style = MaterialTheme.typography.titleMedium.codeStyle(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MissingDay(
    dayNumber: Int,
    dayState: DayState.MissingInformation,
    onCancel: () -> Unit,
    onMissingInformationComplete: () -> Unit,
) {
    DialogWindow(
        onCloseRequest = onCancel,
        undecorated = true,
        resizable = true,
        transparent = true,
        alwaysOnTop = true,
        state = rememberDialogState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(width = 800.dp, height = 600.dp)
        )
    ) {
        val missingInformationPresenter: MissingInformationPresenter = koinInject {
            parametersOf(dayState, dayNumber)
        }

        val uiState by missingInformationPresenter.uiModel.collectAsState()

        LaunchedEffect(uiState.state) {
            if (uiState.state == MissingState.Done) {
                onMissingInformationComplete()
            }
        }

        AdventTheme {
            WindowDraggableArea {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Day $dayNumber - ${dayState.part.display}")
                            },
                            colors = TopAppBarDefaults.topAppBarColors()
                                .copy(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                            actions = {
                                TooltipArea(tooltip = {
                                    Text(text = "Cancel")
                                }) {
                                    IconButton(onClick = onCancel) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Cancel",
                                        )
                                    }
                                }
                                TooltipArea(tooltip = {
                                    Text(text = "Validate")
                                }) {
                                    TextButton(onClick = {
                                        missingInformationPresenter.validate()
                                    }, enabled = uiState.isValid) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Validate"
                                        )
                                    }
                                }
                            })
                    }) {
                    val focusRequester = remember { FocusRequester() }
                    Column(
                        modifier = Modifier.padding(it)
                            .padding(vertical = 24.dp, horizontal = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (uiState.debug) {
                            InputWithLabel(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Debug input",
                                description = "Example input",
                                input = uiState.debugInput.orEmpty(),
                                onTextChanged = missingInformationPresenter::onDebugInputChanged,
                            )
                        }
                        if (uiState.result) {
                            InputWithLabel(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Expected result",
                                description = "What should be the result for the debug input?",
                                input = uiState.expectedResultInput.orEmpty(),
                                onTextChanged = missingInformationPresenter::onExpectedResultInputChanged,
                            )
                        }
                        if (uiState.sessionCookie) {
                            InputWithLabel(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Session cookie",
                                description = """
                 No property named "session" in file ${(TextColors.black on TextColors.blue)("inputs > config.properties")}.
                 Please get the value from your browser when accessing advent of code website by inspecting the webpage on ${
                                    (TextColors.black on TextColors.blue)(
                                        "chrome > Application > Cookies"
                                    )
                                }, and paste the value here of the cookie named ${
                                    (TextColors.black on TextColors.blue)(
                                        "session"
                                    )
                                }""",
                                input = uiState.sessionCookieInput.orEmpty(),
                                onTextChanged = missingInformationPresenter::onSessionCookieInputChanged
                            )
                        }
                        if (uiState.state == MissingState.Error) {
                            Text(
                                text = "There was an error processing your data, please try again",
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ErrorDay(onRetry: () -> Unit, modifier: Modifier = Modifier, message: String?) {
    Column(modifier = modifier) {
        Text(text = "Error")
        message?.let {
            Text(text = it)
        }
        ElevatedButton(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun LoadingDay(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator()
    }
}

@Composable
fun RunningDayProgress(
    modifier: Modifier = Modifier,
    dayState: DayState.DayUiModel,
    onStop: () -> Unit,
    durationState: Duration?
) {
    Column(modifier = modifier) {
        LinearProgressIndicator(
            progress = {
                if (dayState.maxProgress == 0) 0f else
                    (dayState.progress.toFloat() / dayState.maxProgress)
            },
        )
        Row {
            durationState?.let { duration ->
                Text(text = duration.toString(unit = DurationUnit.SECONDS, decimals = 3))
            }
            IconButton(onClick = onStop) {
                Icon(imageVector = Icons.Filled.Stop, contentDescription = "Stop")
            }
        }
    }
}