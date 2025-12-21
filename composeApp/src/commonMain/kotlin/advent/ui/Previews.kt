package advent.ui

import advent.Part
import advent.ui.input.AskInput
import advent.ui.input.AskInputContent
import advent.ui.running.RunningDayContent
import advent.ui.theme.AdventTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun AskInputPreview() = AdventTheme {
    AskInput(
        dayNumber = 1,
        part = Part.ONE,
        isDebug = false,
        onChangeDay = {},
        onChangePart = {},
    ) {}
}

@Preview
@Composable
fun AskInputContentPreview() = AdventTheme {
    AskInputContent(
        dayNumber = 1,
        part = Part.ONE,
        isDebug = true,
        onChangeDay = {},
        onChangeDebug = {},
        onChangePart = {},
    )
}

@Preview
@Composable
fun RunningDayContentPreview() = AdventTheme {
    RunningDayContent()
}