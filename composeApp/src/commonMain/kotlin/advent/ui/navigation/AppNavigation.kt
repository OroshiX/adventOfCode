package advent.ui.navigation

import advent.ui.input.AskInputScreen
import advent.ui.running.RunningDayScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
data object SelectDay

@Serializable
data class DayRunning(val dayNumber: Int, val part: Int, val debug: Boolean, val year: Int)


@OptIn(KoinExperimentalAPI::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SelectDay) {
        composable<SelectDay> {
            AskInputScreen(onRunDay = {
                navController.navigate(it)
            })
        }
        composable<DayRunning> {
            val dayRunning: DayRunning = it.toRoute()
            RunningDayScreen(
                dayRunning = dayRunning,
                onBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}