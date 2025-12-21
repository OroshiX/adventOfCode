package advent

import advent.di.appModules
import advent.ui.App
import advent.ui.config.ConfigManipulator
import adventofcode.composeapp.generated.resources.Res
import adventofcode.composeapp.generated.resources.favicon
import adventofcode.composeapp.generated.resources.sapin_advent_of_code_landscape
import androidx.compose.foundation.Image
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.awaitApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import kotlin.time.Duration.Companion.seconds

fun main() = runBlocking {
    startKoin {
        modules(appModules())
    }
    awaitApplication {
        val configManipulator = koinInject<ConfigManipulator>()
        val title = remember(configManipulator.year) { "Advent of Code ${configManipulator.year}" }
        var splash by remember { mutableStateOf(true) }
        LaunchedEffect(true) {
            delay(2.seconds)
            splash = false
        }
        if (splash) {
            Window(
                onCloseRequest = ::exitApplication,
                undecorated = true,
                title = title,
                transparent = true
            ) {
                WindowDraggableArea {
                    Image(
                        painterResource(Res.drawable.sapin_advent_of_code_landscape),
                        contentDescription = "splash"
                    )
                }
            }
        } else {
            Window(
                onCloseRequest = ::exitApplication,
                undecorated = true,
                title = title,
                icon = painterResource(Res.drawable.favicon)
            ) {
                WindowDraggableArea {
                    App()
                }
            }
        }
    }
}