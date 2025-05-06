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
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BtAppBar(
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTalkFlowClick: () -> Unit = {}
) {
    val selectedIndex = remember { mutableStateOf(0) }

    val items = listOf(
        Triple("Historial", Icons.Default.Home, onHomeClick),
        Triple("Perfil", Icons.Default.Person, onProfileClick),
        Triple("TalkFlow", Icons.Default.Settings, onTalkFlowClick)
    )

    NavigationBar {
        items.forEachIndexed { index, (label, icon, action) ->
            NavigationBarItem(
                icon = { Icon(imageVector = icon, contentDescription = label) },
                label = { Text(label) },
                selected = selectedIndex.value == index,
                onClick = {
                    selectedIndex.value = index
                    action()
                }
            )
        }
    }
}
