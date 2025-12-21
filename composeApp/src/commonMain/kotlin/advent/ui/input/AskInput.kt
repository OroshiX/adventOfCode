package advent.ui.input

import advent.Part
import advent.ui.navigation.DayRunning
import adventofcode.composeapp.generated.resources.Res
import adventofcode.composeapp.generated.resources.sapin_cadeaux_advent_of_code_simple
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun AskInputScreen(onRunDay: (DayRunning) -> Unit) {
    val presenter: AskInputPresenter = koinInject()
    val uiModel by presenter.inputState.collectAsState()
    AskInputContent(
        dayNumber = uiModel.dayNumber,
        part = uiModel.part,
        isDebug = uiModel.isDebug,
        year = uiModel.year,
        onChangeDay = presenter::changeDay,
        onChangePart = presenter::changePart,
        onChangeYear = presenter::changeYear,
        onChangeDebug = presenter::changeData,
        onValidate = {
            val navigateTo: DayRunning = presenter.validate()
            onRunDay(navigateTo)
        },
    )
}

@Composable
fun AskInputContent(
    dayNumber: Int,
    part: Part,
    year: Int,
    isDebug: Boolean,
    onChangeDay: (Int) -> Unit = {},
    onChangePart: (Part) -> Unit = {},
    onChangeDebug: (Boolean) -> Unit = {},
    onValidate: () -> Unit = {},
    onChangeYear: (Int) -> Unit = {},
) {
    var height by remember { mutableStateOf(0) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            modifier = Modifier.onGloballyPositioned {
                height = it.size.height
            },
            painter = painterResource(Res.drawable.sapin_cadeaux_advent_of_code_simple),
            contentDescription = "Sapin de Noël décoré",
        )
        val heightDp = with(LocalDensity.current) {
            height.toDp()
        }
        AskInput(
            modifier = Modifier.height(heightDp).padding(vertical = 16.dp),
            dayNumber = dayNumber,
            part = part,
            year = year,
            isDebug = isDebug,
            onChangeDay = onChangeDay,
            onChangePart = onChangePart,
            onChangeDebug = onChangeDebug,
            onChangeYear = onChangeYear,
            onValidate = onValidate,
        )
    }
}

@Composable
internal fun AskInput(
    modifier: Modifier = Modifier,
    dayNumber: Int,
    part: Part,
    year: Int,
    isDebug: Boolean,
    onChangeDay: (Int) -> Unit = {},
    onChangePart: (Part) -> Unit = {},
    onChangeYear: (Int) -> Unit = {},
    onChangeDebug: (Boolean) -> Unit = {},
    onValidate: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxWidth().padding(end = 8.dp, bottom = 8.dp)) {
        Column {
            LabelDropdown(
                modifier = Modifier.padding(bottom = 16.dp),
                title = "Year",
                selected = year,
                dropdownOptions = (2020..2030).toList(),
                onChooseOption = { onChangeYear(it) },
                toLabel = { it.toString() },
            )
            LabelDropdown(
                title = "Day",
                selected = dayNumber,
                dropdownOptions = (1..25).toList(),
                onChooseOption = { onChangeDay(it) },
                toLabel = { it.toString() },
            )

            LabelDropdown(
                modifier = Modifier.padding(vertical = 16.dp),
                title = "Part",
                selected = part,
                dropdownOptions = Part.entries,
                onChooseOption = { onChangePart(it) },
                toLabel = { it.display }
            )
            LabelDropdown(
                title = "Data",
                selected = isDebug,
                dropdownOptions = listOf(true, false),
                toLabel = { if (it) "Debug" else "Real data" },
                onChooseOption = {
                    onChangeDebug(it)
                })
        }
        OutlinedButton(
            modifier = Modifier.padding(top = 16.dp).align(Alignment.BottomEnd),
            onClick = onValidate,
        ) {
            Text(text = "Validate")
            Icon(imageVector = Icons.Filled.Check, contentDescription = "Validate")
        }
    }
}

@Composable
private fun <T> LabelDropdown(
    modifier: Modifier = Modifier,
    title: String,
    selected: T,
    dropdownOptions: List<T>,
    onChooseOption: (T) -> Unit,
    toLabel: (T) -> String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(modifier = Modifier.width(150.dp), text = title)
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium,
            ).clickable {
                expanded = !expanded
            }.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                modifier = Modifier,
                text = toLabel(selected)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                scrollState = rememberScrollState(),
            ) {
                dropdownOptions.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.background(
                            color = if (selected == option) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        text = { Text(text = toLabel(option)) },
                        onClick = {
                            onChooseOption(option)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun InputWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    input: String,
    onTextChanged: (String) -> Unit
) {
    val textFieldState = rememberTextFieldState(initialText = input)
    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text }.collect {
            onTextChanged(it.toString())
        }
    }
    Column(modifier = modifier) {
        Text(text = label)
        TextField(state = textFieldState)
    }
}