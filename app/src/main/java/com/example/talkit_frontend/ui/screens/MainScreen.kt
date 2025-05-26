package com.example.talkit_frontend.ui.screens


import android.os.Build
import android.os.Bundle


import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.data.SessionManager
import com.example.talkit_frontend.ui.components.BtAppBar
import com.example.talkit_frontend.ui.components.ConfirmationButton
import com.example.talkit_frontend.ui.components.DropdownSelector
import com.example.talkit_frontend.ui.components.SceneButton

import com.example.talkit_frontend.ui.navigation.AppNavigation

import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Talkit_frontendTheme {
                AppNavigation()
            }
        }

    }
}

@Composable
fun Mainscreen(navController: NavController) {

    val context = LocalContext.current

    var selectedScene by remember { mutableStateOf<String?>(null) }
    var selectedChatLanguageName by remember { mutableStateOf("Inglés") }
    var selectedChatLanguageCode by remember { mutableStateOf("en-US") }
    var selectedSpokenLanguageName by remember { mutableStateOf("Español") }
    var selectedSpokenLanguageCode by remember { mutableStateOf("es-ES") }


    val idiomas = mapOf(
        "Español" to "es-ES",
        "Inglés" to "en-US",
        "Francés" to "fr-FR",
        "Alemán" to "de-DE",
        "Italiano" to "it-IT",
        "Búlgaro" to "bg-BG"
    )

    Scaffold(bottomBar = { BtAppBar(navController) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Escoge un escenario",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(start = 8.dp, top = 16.dp, bottom = 4.dp)
                    )
                    Text(
                        text = "Selecciona el entorno donde practicarás la conversación.",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                }

                val scenes = listOf("Restaurante", "Aeropuerto", "Hospital", "Hotel")
                scenes.forEach { scene ->
                    val isSelected = scene == selectedScene

                    SceneButton(
                        text = scene,
                        isSelected = isSelected,
                        onClick = {
                            selectedScene = if (isSelected) null else scene
                            Toast.makeText(context, "Seleccionado: $scene", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                DropdownSelector(
                    label = "Selecciona idioma de traducción:",
                    idiomas = idiomas.keys.toList(),
                    selectedOption = selectedChatLanguageName,
                    onOptionSelected = { name ->
                        selectedChatLanguageName = name
                        selectedChatLanguageCode = idiomas[name] ?: "en-US"
                    }
                )

                DropdownSelector(
                    label = "Selecciona idioma de habla:",
                    idiomas = idiomas.keys.toList(),
                    selectedOption = selectedSpokenLanguageName,
                    onOptionSelected = { name ->
                        selectedSpokenLanguageName = name
                        selectedSpokenLanguageCode = idiomas[name] ?: "es-ES"
                    }
                )

                Spacer(modifier = Modifier.padding(16.dp))

                ConfirmationButton(
                    text = "Comenzar",
                    onClick = {
                        if (selectedScene?.isNotBlank() == true && selectedChatLanguageCode.isNotBlank() && selectedSpokenLanguageCode.isNotBlank()) {
                            Toast.makeText(
                                context,
                                "Escena: $selectedScene",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            navController.navigate("chat/${selectedScene}/${selectedChatLanguageCode}/${selectedSpokenLanguageCode}")
                        } else {
                            Toast.makeText(context, "Selecciona una escena", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                )


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Talkit_frontendTheme {
        Mainscreen(navController = rememberNavController())
    }
}