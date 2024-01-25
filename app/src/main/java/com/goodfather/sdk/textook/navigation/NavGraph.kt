package com.goodfather.sdk.textook.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.goodfather.sdk.textook.ui.pages.SplashPage

@Composable
fun NavGraph(
    navigationController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navigationController, startDestination = Screen.SplashPage.route) {
        composable(Screen.SplashPage.route) {
            SplashPage(
                onBack = {
                    NavigationAction.onBack(navigationController)
                }
            )
        }
    }
}

class NavigationAction {
    companion object {
        fun onBack(navigationController: NavHostController) {
            navigationController.navigateUp()
        }
    }
}