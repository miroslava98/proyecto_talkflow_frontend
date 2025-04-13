package com.example.talkit_frontend.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talkit_frontend.ui.components.BtAppBar
import com.example.talkit_frontend.ui.components.SceneButton
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme

class TranslateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Talkit_frontendTheme {
                TranslateScreen()
            }
        }
    }
}

@Composable
fun TranslateScreen() {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BtAppBar(
                onHomeClick = {
                    Toast.makeText(context, "Inicio", Toast.LENGTH_SHORT).show()
                },
                onProfileClick = {
                    Toast.makeText(context, "Perfil", Toast.LENGTH_SHORT).show()
                },
                onSettingsClick = {
                    Toast.makeText(context, "Ajustes", Toast.LENGTH_SHORT).show()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text("Elige una escena")


            SceneButton(
                text = "Restaurante",
                onClick = { Toast.makeText(context, "clickeado", Toast.LENGTH_LONG).show() })

            SceneButton(
                text = "Aeropuerto",
                onClick = { Toast.makeText(context, "clickeado", Toast.LENGTH_LONG).show() })

            SceneButton(
                text = "Peluquería",
                onClick = { Toast.makeText(context, "clickeado", Toast.LENGTH_LONG).show() })

            SceneButton(
                text = "Supermercado",
                onClick = { Toast.makeText(context, "clickeado", Toast.LENGTH_LONG).show() })

        }
    }

}

@Preview(showBackground = true)
@Composable
fun TranslateScreenPreview() {
    Talkit_frontendTheme {
        TranslateScreen()
    }
}