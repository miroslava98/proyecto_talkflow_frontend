package com.example.talkit_frontend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.ui.screens.LoginScreen
import com.example.talkit_frontend.ui.screens.Mainscreen
import com.example.talkit_frontend.ui.screens.RecordsScreen
import com.example.talkit_frontend.ui.screens.RegisterScreen
import com.example.talkit_frontend.ui.screens.UpdateProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {

        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.RegisterScreen.route) {
            RegisterScreen()
        }
        composable(route = AppScreens.MainScreen.route) {
            Mainscreen(navController)
        }

        composable(route = AppScreens.RecordsScreen.route) {
            RecordsScreen(navController)
        }

        composable(route = AppScreens.UpdateProfileScreen.route) {
            UpdateProfileScreen(navController)
        }

    }
}

