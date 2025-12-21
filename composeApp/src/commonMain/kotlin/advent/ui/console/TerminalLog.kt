package advent.ui.console

import advent.ui.theme.AdventTheme
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
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TerminalLog(modifier: Modifier = Modifier, logsLines: List<LogLine>) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        LazyColumn(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            items(items = logsLines) {
                Text(text = it.text, color = it.color)
            }
        }
    }
}

data class LogLine(val text: String, val color: Color)

@Composable
@Preview
fun TerminalLogPreview() = AdventTheme {
    TerminalLog(
        logsLines = listOf(
            LogLine("toto", Color(0xffababab)),
            LogLine("tata", Color(0xffafe212))
        )
    )
}