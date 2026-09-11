package com.squigo.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.squigo.feature.habitdetail.HabitDetailScreen
import com.squigo.feature.habits.HabitsScreen

private const val ROUTE_HABITS = "habits"
private const val ROUTE_HABIT_DETAIL = "habits/{habitId}"
private const val ARG_HABIT_ID = "habitId"

@Composable
fun SquigoNavHost() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = ROUTE_HABITS) {
    composable(ROUTE_HABITS) {
      HabitsScreen(onOpenHabit = { habitId -> navController.navigate("habits/$habitId") })
    }

    composable(
      route = ROUTE_HABIT_DETAIL,
      arguments = listOf(navArgument(ARG_HABIT_ID) { type = NavType.LongType }),
    ) { backStackEntry ->
      val habitId = backStackEntry.arguments?.getLong(ARG_HABIT_ID) ?: return@composable
      HabitDetailScreen(habitId = habitId, onNavigateUp = { navController.popBackStack() })
    }
  }
}
