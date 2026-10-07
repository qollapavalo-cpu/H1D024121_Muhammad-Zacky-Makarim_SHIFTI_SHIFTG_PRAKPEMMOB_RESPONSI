package com.pemmob.pipitanime.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.pipitanime.ui.detail.DetailScreen
import com.pemmob.pipitanime.ui.home.HomeScreen

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{malId}"
    fun detail(malId: Int) = "detail/$malId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(onAnimeClick = { id -> navController.navigate(Routes.detail(id)) })
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("malId") { type = NavType.IntType })
        ) { entry ->
            val malId = entry.arguments?.getInt("malId") ?: 0
            DetailScreen(malId = malId, onBack = { navController.popBackStack() })
        }
    }
}
