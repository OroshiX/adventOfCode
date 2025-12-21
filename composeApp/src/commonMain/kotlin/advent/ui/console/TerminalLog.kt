package advent.ui.console

import advent.ui.theme.codeStyle
import advent.ui.theme.consoleContainer
import advent.ui.theme.onConsoleContainer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TerminalLog(modifier: Modifier = Modifier, logsLines: List<LogLine>) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.consoleContainer()
    ) {
        LazyColumn(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            items(items = logsLines) {
                Text(
                    text = it.text,
                    color = if (it.color == Color.Unspecified) MaterialTheme.colorScheme.onConsoleContainer() else it.color,
                    style = MaterialTheme.typography.bodyMedium.codeStyle()
                )
            }
        }
    }
}

data class LogLine(val text: String, val color: Color = Color.Unspecified)

