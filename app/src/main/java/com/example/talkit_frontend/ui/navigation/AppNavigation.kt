package com.example.talkit_frontend.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.data.SessionManager
import com.example.talkit_frontend.ui.screens.AnimatedSplashScreen
import com.example.talkit_frontend.ui.screens.ChatScreen
import com.example.talkit_frontend.ui.screens.LoginScreen
import com.example.talkit_frontend.ui.screens.Mainscreen
import com.example.talkit_frontend.ui.screens.ProfileSettingsScreen
import com.example.talkit_frontend.ui.screens.RecordsScreen
import com.example.talkit_frontend.ui.screens.RegisterScreen
import com.example.talkit_frontend.ui.screens.UpdateProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val authTokenFlow = remember { sessionManager.authToken }
    val authToken by authTokenFlow.collectAsState(initial = null)

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route) {

        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(route = AppScreens.MainScreen.route) {
            Mainscreen(navController)
        }
        composable(route = AppScreens.SplashScreen.route) {
            AnimatedSplashScreen(navController)
        }

        composable(AppScreens.RecordsScreen.route) {
            if (authToken.isNullOrBlank()) {
                LaunchedEffect(Unit) {
                    navController.navigate(AppScreens.LoginScreen.route) {
                        // Mantener MainScreen en la pila para volver atrás
                        popUpTo(AppScreens.MainScreen.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            } else {
                RecordsScreen(navController)
            }
        }

        composable(AppScreens.ProfileSettingsScreen.route) {
            if (authToken.isNullOrBlank()) {
                LaunchedEffect(Unit) {
                    navController.navigate(AppScreens.LoginScreen.route) {
                        popUpTo(AppScreens.MainScreen.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            } else {
                ProfileSettingsScreen(navController)
            }
        }
        composable(route = AppScreens.UpdateProfileScreen.route) {
            UpdateProfileScreen(navController)
        }

        composable(route = "chat/{scene}/{languageChat}/{languageSpoken}") { backStackEntry ->
            val scene = backStackEntry.arguments?.getString("scene") ?: ""
            val languageChat = backStackEntry.arguments?.getString("languageChat") ?: ""
            val languageSpoken = backStackEntry.arguments?.getString("languageSpoken") ?: ""
            ChatScreen(scene, languageChat, languageSpoken, navController)

        }
    }
}

@Composable
fun AuthenticatedOnly(
    navController: NavController,
    sessionManager: SessionManager,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val authTokenFlow = remember { sessionManager.authToken }
    val authToken by authTokenFlow.collectAsState(initial = null)

    LaunchedEffect(authToken) {
        if (authToken.isNullOrBlank()) {
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(0)
            }
        }
    }

    if (!authToken.isNullOrBlank()) {
        content()
    }
}


