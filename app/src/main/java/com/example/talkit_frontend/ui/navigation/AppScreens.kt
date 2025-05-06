package com.example.talkit_frontend.ui.navigation

sealed class AppScreens(val route: String) {
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object MainScreen : AppScreens("main_screen")

}