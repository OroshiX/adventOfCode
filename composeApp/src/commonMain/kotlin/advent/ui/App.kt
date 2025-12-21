package advent.ui

import advent.ui.navigation.AppNavigation
import advent.ui.theme.AdventTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AdventTheme {
        Scaffold {
            AppNavigation()
        }
    }
}
