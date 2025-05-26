package com.example.talkit_frontend.ui.navigation

sealed class AppScreens(val route: String) {
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object MainScreen : AppScreens("main_screen")
    object RecordsScreen : AppScreens("records_screen")
    object UpdateProfileScreen : AppScreens("updateprofile_screen")
    object ProfileSettingsScreen : AppScreens("profilesettings_screen")
    object ChatScreen : AppScreens("chat_screen")
    object SplashScreen : AppScreens("splash_screen")

}