package com.example.talkit_frontend.ui.screens

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talkit_frontend.R
import com.example.talkit_frontend.ui.components.BtAppBar
import com.example.talkit_frontend.ui.components.ProfileTextField
import com.example.talkit_frontend.ui.components.SceneButton
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme

class UpdateProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Talkit_frontendTheme {
                UpdateProfileScreen()
            }
        }
    }
}

@Composable
fun UpdateProfileScreen() {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BtAppBar(
                onHomeClick = {},
                onProfileClick = { Toast.makeText(context, "Perfil", Toast.LENGTH_SHORT).show() },
                onTalkFlowClick = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFB2A1FF),
                            Color(0xFF8A80FF)
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // Imagen de perfil
            Image(
                painter = painterResource(id = R.drawable.avatar_prueba), // usa tu recurso
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campos de entrada
            ProfileTextField(
                label = "Nombre",
                placeholder = "Introduce nombre",
                value = nombre,
                onValueChange = { nombre = it },
                onClear = { nombre = "" }
            )

            ProfileTextField(
                label = "Correo",
                placeholder = "Introduce correo",
                value = email,
                onValueChange = { email = it },
                onClear = { email = "" }
            )

            ProfileTextField(
                label = "Contraseña actual",
                placeholder = "Introduce contraseña actual",
                value = currentPassword,
                onValueChange = { currentPassword = it },
                onClear = { currentPassword = "" },
                isPassword = true
            )

            ProfileTextField(
                label = "Contraseña Nueva",
                placeholder = "Introduce nueva contraseña",
                value = newPassword,
                onValueChange = { newPassword = it },
                onClear = { newPassword = "" },
                isPassword = true
            )

            ProfileTextField(
                label = "Confirmar Contraseña",
                placeholder = "Confirma la contraseña",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                onClear = { confirmPassword = "" },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Actualizar
            SceneButton(
                text = "Actualizar",
                onClick = {
                    Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                }

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateProfileScreenPreview() {
    Talkit_frontendTheme {
        UpdateProfileScreen()
    }
}