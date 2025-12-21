package advent.ui.navigation

import advent.ui.input.AskInputScreen
import advent.ui.running.RunningDayScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.core.annotation.KoinExperimentalAPI

enum class AdventScreens(val displayName: String, val route: String) {
    SELECT_DAY("Select a day", "day"),
    RUNNING("Solving the day", "running")
}


@OptIn(KoinExperimentalAPI::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AdventScreens.SELECT_DAY.route) {
        composable(route = AdventScreens.SELECT_DAY.route) {
            AskInputScreen(onRunDay = {
                navController.navigate(route = AdventScreens.RUNNING.route)
            })
        }
        composable(route = AdventScreens.RUNNING.route) {
            RunningDayScreen(onBack = {
                navController.popBackStack()
            })
        }
    }
}