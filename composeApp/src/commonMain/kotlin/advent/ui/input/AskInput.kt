package advent.ui.input

import advent.Part
import adventofcode.composeapp.generated.resources.Res
import adventofcode.composeapp.generated.resources.sapin_cadeaux_advent_of_code_simple
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun AskInputScreen(onRunDay: () -> Unit) {
    val presenter: AskInputPresenter = koinInject()
    val uiModel by presenter.inputState.collectAsState()
    AskInputContent(
        dayNumber = uiModel.dayNumber,
        part = uiModel.part,
        isDebug = uiModel.isDebug,
        onChangeDay = presenter::changeDay,
        onChangePart = presenter::changePart,
        onChangeDebug = presenter::changeData,
        onValidate = {
            presenter.validate()
            onRunDay()
        },
    )

}

@Composable
fun AskInputContent(
    dayNumber: Int,
    part: Part,
    isDebug: Boolean,
    onChangeDay: (Int) -> Unit = {},
    onChangePart: (Part) -> Unit = {},
    onChangeDebug: (Boolean) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    Row {
        Image(
            painter = painterResource(Res.drawable.sapin_cadeaux_advent_of_code_simple),
            contentDescription = "Sapin de Noël décoré",
        )
        AskInput(
            dayNumber = dayNumber,
            part = part,
            isDebug = isDebug,
            onChangeDay = onChangeDay,
            onChangePart = onChangePart,
            onChangeDebug = onChangeDebug,
            onValidate = onValidate,
        )
    }
}

@Composable
internal fun AskInput(
    dayNumber: Int,
    part: Part,
    isDebug: Boolean,
    onChangeDay: (Int) -> Unit = {},
    onChangePart: (Part) -> Unit = {},
    onChangeDebug: (Boolean) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Day number",
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                modifier = Modifier.weight(2f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { onChangeDay(dayNumber - 1) }) {
                    Icon(imageVector = Icons.Filled.Remove, contentDescription = "Remove")
                }
                Text(dayNumber.toString(), style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { onChangeDay(dayNumber + 1) }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Part",
                style = MaterialTheme.typography.bodyLarge
            )
            Box(modifier = Modifier.weight(2f)) {
                var expanded by remember { mutableStateOf(false) }
                Text(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.medium,
                    ).clickable {
                        expanded = !expanded
                    }.padding(horizontal = 4.dp, vertical = 2.dp),
                    text = part.display
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    Part.entries.forEach { part ->
                        DropdownMenuItem(text = {
                            Text(text = part.display)
                        }, onClick = {
                            onChangePart(part)
                            expanded = false
                        })
                    }
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.weight(1f), text = "Data")
            Box(modifier = Modifier.weight(2f)) {
                var expanded by remember { mutableStateOf(false) }
                Text(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.medium,
                    ).clickable {
                        expanded = !expanded
                    }.padding(horizontal = 4.dp, vertical = 2.dp),
                    text = if (isDebug) "Debug" else "Real data"
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    repeat(2) {
                        DropdownMenuItem(text = {
                            Text(text = if (it == 1) "Debug" else "Real data")
                        }, onClick = {
                            onChangeDebug(it == 1)
                            expanded = false
                        })
                    }
                }
            }
        }

        AnimatedVisibility(visible = isDebug) {
            // TODO: give real values for params
            DebugInput(debugInput = "TODO", editing = false)
        }

        OutlinedIconButton(
            modifier = Modifier.padding(top = 16.dp),
            onClick = onValidate,
        ) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "Validate")
        }
    }
}

@Composable
fun DebugInput(modifier: Modifier = Modifier, debugInput: String, editing: Boolean) {
    Crossfade(modifier = modifier, targetState = editing) {
        if (it) {
            val textFieldState = rememberTextFieldState(initialText = debugInput)
            TextField(state = textFieldState)
        } else {
            Text(text = debugInput)
        }
    }
}