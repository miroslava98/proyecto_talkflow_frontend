package com.example.talkit_frontend.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talkit_frontend.R
import com.example.talkit_frontend.ui.components.EmailTextField
import com.example.talkit_frontend.ui.components.PasswordTextField
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Talkit_frontendTheme {
                RegisterScreen()
            }
        }

    }
}

@Composable
fun RegisterScreen() {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }
    var confirmar_contrasenya by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Image(
            painter = painterResource(id = R.drawable.avatar_prueba),
            contentDescription = "imagen de avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        )


        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )
        EmailTextField(value = correo, onValueChange = { correo = it })

        PasswordTextField(value = contrasenya, onValueChange = { contrasenya = it })
        PasswordTextField(
            value = confirmar_contrasenya,
            onValueChange = { confirmar_contrasenya = it },
            label = "Confirmar Contraseña"
        )
        FilledTonalButton(
            onClick = { println("Apretado") }
        ) {
            Text("Registrar")
        }
    }


}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    Talkit_frontendTheme {
        RegisterScreen()
    }
}