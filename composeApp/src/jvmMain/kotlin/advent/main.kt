package advent

import advent.di.appModules
import advent.ui.App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(appModules())
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "adventOfCode",
    ) {
        App()
    }
}
