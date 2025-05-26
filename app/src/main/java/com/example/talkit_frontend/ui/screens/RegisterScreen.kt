package com.example.talkit_frontend.ui.screens

import BirthDatePicker
import RegisterRequest
import RetrofitClient
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.R
import com.example.talkit_frontend.ui.components.ConfirmationButton
import com.example.talkit_frontend.ui.components.EmailTextField
import com.example.talkit_frontend.ui.components.PasswordTextField
import com.example.talkit_frontend.ui.navigation.AppNavigation
import com.example.talkit_frontend.ui.navigation.AppScreens
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RegisterActivity : ComponentActivity() {
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var fecha_nacimiento by remember { mutableStateOf<LocalDate?>(null) }
    var contrasenya by remember { mutableStateOf("") }
    var confirmar_contrasenya by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


    val handleRegister = {
        if (nombre.isNotEmpty() && correo.isNotEmpty() && fecha_nacimiento != null && contrasenya.isNotEmpty() && confirmar_contrasenya.isNotEmpty()) {
            fecha_nacimiento?.let { date ->

                val registerRequest =
                    RegisterRequest(nombre, correo, fecha_nacimiento!!, contrasenya, avatar)

                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        val response = RetrofitClient.apiService.register(registerRequest)
                        if (response.isSuccessful && response.body() != null) {
                            val result = response.body()
                            withContext(Dispatchers.Main) {
                                showDialog = true
                                navController.navigate(AppScreens.LoginScreen.route)

                            }


                        }

                    } catch (e: Exception) {
                        errorMessage = "Error de conexión. Intenta de nuevo."
                    }
                }


            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Verifica tu correo") },
            text = { Text("Hemos enviado un enlace de verificación a tu correo electrónico. Por favor, verifica tu cuenta antes de iniciar sesión.") },
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        showDialog = false
                        navController.navigate(AppScreens.LoginScreen.route)
                    }
                ) {
                    Text("Ir al inicio de sesión")
                }
            }
        )
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.50f)),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar_prueba),
                contentDescription = "imagen de avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )

        }
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                focusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                cursorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),

                )
        )
        EmailTextField(value = correo, onValueChange = { correo = it })

        Box(modifier = Modifier.fillMaxWidth()) {
            BirthDatePicker(
                selectedDate = fecha_nacimiento,
                onDateSelected = { fecha_nacimiento = it }

            )
        }

        PasswordTextField(value = contrasenya, onValueChange = { contrasenya = it })
        PasswordTextField(
            value = confirmar_contrasenya,
            onValueChange = { confirmar_contrasenya = it },
            label = "Confirmar Contraseña"
        )
        ConfirmationButton(
            text = "Registrarse",
            onClick = {
                if (nombre.isBlank() || correo.isBlank() || fecha_nacimiento == null || contrasenya.isBlank() || confirmar_contrasenya.isBlank()) {
                    errorMessage = "Por favor, completa todos los campos"
                } else if (contrasenya != confirmar_contrasenya) {
                    errorMessage = "Las contraseñas no coinciden"
                } else {
                    errorMessage = ""
                    // Tu lógica de registro aquí
                }
            }
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    Talkit_frontendTheme {
        RegisterScreen(navController = rememberNavController())
    }
}