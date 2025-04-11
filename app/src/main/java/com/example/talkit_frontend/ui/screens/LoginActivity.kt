package com.example.talkit_frontend.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talkit_frontend.ui.components.EmailTextField
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Talkit_frontendTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    var correo by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmailTextField(value = correo,
            onValueChange = { correo = it })
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(),
        )
        OutlinedTextField(value = contrasenya,
            onValueChange = { contrasenya = it },
            label = { Text("Contraseña") }
        )
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(),
        )
        FilledTonalButton(
            onClick = { println("Apretado") }
        ) {
            Text("Entrar")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Talkit_frontendTheme {
        LoginScreen()

    }
}