package advent.ui

import advent.Part
import advent.ui.console.LogLine
import advent.ui.console.TerminalLog
import advent.ui.input.AskInput
import advent.ui.input.AskInputContent
import advent.ui.input.InputWithLabel
import advent.ui.running.RunningDayContent
import advent.ui.theme.AdventTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun AskInputPreview() = AdventTheme {
    AskInput(
        dayNumber = 1,
        part = Part.ONE,
        isDebug = false,
        year = 2025,
    )
}

@Preview
@Composable
fun AskInputContentPreview() = AdventTheme {
    AskInputContent(
        dayNumber = 1,
        part = Part.ONE,
        isDebug = true,
        year = 2025,
    )
}

@Preview
@Composable
fun InputWithLabelPreview() = AdventTheme {
    InputWithLabel(input = "My debug input", label = "Input", onTextChanged = {})
}

@Composable
@Preview
fun TerminalLogPreview() = AdventTheme {
    TerminalLog(
        logsLines = listOf(
            LogLine("The input was\n[.##.] (3) (1,3) {1}", Color(0xffababab)),
            LogLine("tata", Color(0xffafe212)),
            LogLine("No color specified")
        )
    )
}

@Preview
@Composable
fun RunningDayContentPreview() = AdventTheme {
    RunningDayContent()
}