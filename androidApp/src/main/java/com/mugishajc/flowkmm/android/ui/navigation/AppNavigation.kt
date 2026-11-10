package com.mugishajc.flowkmm.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mugishajc.flowkmm.android.ui.screens.HabitDetailScreen
import com.mugishajc.flowkmm.android.ui.screens.HabitListScreen
import com.mugishajc.flowkmm.android.ui.screens.HabitStatsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "habits") {
        composable("habits") {
            HabitListScreen(
                onHabitClick = { habitId ->
                    navController.navigate("habit/$habitId")
                },
                onStatsClick = { habitId ->
                    navController.navigate("stats/$habitId")
                },
            )
        }

        composable(
            route = "habit/{habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId") ?: return@composable
            HabitDetailScreen(
                habitId = habitId,
                onBack = { navController.popBackStack() },
                onStatsClick = { navController.navigate("stats/$habitId") },
            )
        }

        composable(
            route = "stats/{habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId") ?: return@composable
            HabitStatsScreen(
                habitId = habitId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
