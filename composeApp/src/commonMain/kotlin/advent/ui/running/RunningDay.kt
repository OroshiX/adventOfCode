package advent.ui.running

import advent.ui.navigation.DayRunning
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Stop
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
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import kotlin.time.Duration
import kotlin.time.DurationUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunningDayScreen(dayRunning: DayRunning, onBack: () -> Unit) {
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
        )
    }
}

@Composable
fun RunningDayContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
    }
}

@Composable
fun RunningDayProgress(progress: Int, maximum: Int, timeElapsed: Duration, onStop: () -> Unit) {
    Column {
        LinearProgressIndicator(
            progress = {
                if (maximum == 0) 0f else
                    (progress.toFloat() / maximum)
            },
        )
        Row {
            Text(text = timeElapsed.toString(unit = DurationUnit.SECONDS, decimals = 3))
            IconButton(onClick = onStop) {
                Icon(imageVector = Icons.Filled.Stop, contentDescription = "Stop")
            }
        }
    }
}