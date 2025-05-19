package com.example.talkit_frontend.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.talkit_frontend.ui.navigation.AppScreens

@Composable
fun BtAppBar(navController: NavController) {
    val selectedIndex = remember { mutableStateOf(0) }

    val items = listOf(
        Triple("TalkFlow", Icons.Default.Settings, AppScreens.MainScreen.route),
        Triple("Historial", Icons.Default.Home, AppScreens.RecordsScreen.route),
        Triple("Perfil", Icons.Default.Person, AppScreens.UpdateProfileScreen.route)
    )

    NavigationBar {
        items.forEachIndexed { index, (label, icon, route) ->
            NavigationBarItem(
                icon = { Icon(imageVector = icon, contentDescription = label) },
                label = { Text(label) },
                selected = selectedIndex.value == index,
                onClick = {
                    selectedIndex.value = index
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
