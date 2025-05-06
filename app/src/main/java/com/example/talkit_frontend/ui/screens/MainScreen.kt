package com.example.talkit_frontend.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talkit_frontend.ui.components.BtAppBar
import com.example.talkit_frontend.ui.components.MicOffIcon
import com.example.talkit_frontend.ui.components.SceneButton
import com.example.talkit_frontend.ui.components.TranscribeTextIcon

import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Talkit_frontendTheme {
                Mainscreen()
            }
        }

    }
}

@Composable
fun Mainscreen() {
    val context = LocalContext.current
    var selectedScene by remember { mutableStateOf("") }
    var showIcons by remember { mutableStateOf(false) } // ← nuevo estado

    Scaffold(
        bottomBar = {
            BtAppBar(
                onHomeClick = {
                    Toast.makeText(context, "Inicio", Toast.LENGTH_SHORT).show()
                },
                onProfileClick = {
                    Toast.makeText(context, "Perfil", Toast.LENGTH_SHORT).show()
                },
                onTalkFlowClick = {

                    Toast.makeText(context, "Ajustes", Toast.LENGTH_SHORT).show()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFB2A1FF), Color(0xFF8A80FF))
                    )
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título
            Text(
                text = "Escoge escenario",
                color = Color.White,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            // Botones de escena
            val scenes = listOf("Restaurante", "Aeropuerto", "Peluquería", "Supermercado")
            scenes.forEach { scene ->
                SceneButton(
                    text = scene,
                    onClick = {
                        selectedScene = scene
                        Toast.makeText(context, "Seleccionado: $scene", Toast.LENGTH_SHORT).show()
                    },

                    )
            }
            Spacer(modifier = Modifier.padding(20.dp))
            // Botón Comenzar
            SceneButton(
                text = "Comenzar",
                onClick = {
                    if (selectedScene.isNotEmpty()) {
                        showIcons = true

                        Toast.makeText(context, "Escena: $selectedScene", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(context, "Selecciona una escena", Toast.LENGTH_SHORT).show()
                    }
                }

            )


        }

        if (showIcons) {

            Spacer(modifier = Modifier.padding(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .padding(top = 250.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                MicOffIcon {
                    // Acción al hacer clic, como:
                    Toast.makeText(context, "Micrófono apagado", Toast.LENGTH_SHORT).show()
                }

                TranscribeTextIcon {
                    Toast.makeText(context, "Transcribir texto", Toast.LENGTH_SHORT).show()
                }

            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Talkit_frontendTheme {
        Mainscreen()
    }
}
