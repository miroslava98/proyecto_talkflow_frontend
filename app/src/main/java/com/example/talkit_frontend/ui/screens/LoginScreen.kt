package com.example.talkit_frontend.ui.screens

import LoginRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.data.SessionManager
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

class LoginActivity : ComponentActivity() {
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
fun LoginScreen(
    navController: NavController,
) {
    // Interceptar botón atrás para que vaya a MainScreen
    BackHandler {
        navController.navigate(AppScreens.MainScreen.route) {
            popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
        }
    }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var correo by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val handleLogin = {
        errorMessage = ""
        // correo = "miv986@hotmail.com"
        // contrasenya = "123456"
        if (correo.isNotEmpty() && contrasenya.isNotEmpty()) {
            isLoading = true

            val loginRequest = LoginRequest(correo, contrasenya)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitClient.apiService.loginUser(loginRequest)
                    if (response.isSuccessful && response.body() != null) {
                        val userResponse = response.body()!!
                        val token = userResponse.token
                        val userName = userResponse.nombre
                        val userEmail = userResponse.email
                        val userFechaNac = userResponse.fechaNacimiento
                        val userAvatar = userResponse.avatar

                        // Aquí podrías guardar el token en SharedPreferences o DataStore
                        sessionManager.saveAuthToken(token)
                        sessionManager.saveUserData(userName, userEmail, userFechaNac ?: "", userAvatar ?: "")

                        // Navegamos a la pantalla principal en el hilo principal
                        withContext(Dispatchers.Main) {
                            navController.navigate(AppScreens.MainScreen.route) {
                                // Evitar que el usuario pueda volver al login con el botón atrás
                                popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                            }
                        }
                    } else {
                        errorMessage = "Credenciales inválidas"
                    }
                } catch (e: Exception) {
                    errorMessage = "Error de conexión. Intenta de nuevo."
                } finally {
                    isLoading = false
                }
            }
        } else {
            errorMessage = "Por favor ingresa ambos campos."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4A5C85), // Más claro que #34446C
                            Color(0xFF34446C)  // Base
                        )
                    )
                ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            EmailTextField(value = correo,
                onValueChange = { correo = it })
            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(),
            )
            PasswordTextField(
                value = contrasenya,
                onValueChange = { contrasenya = it },
                label = "Contraseña"
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            ConfirmationButton(
                text = if (isLoading) "Cargando..." else "Entrar",
                onClick = {
                    if (!isLoading) {
                        handleLogin()
                    }
                },
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "¿No tienes una cuenta? Regístrate aquí",
                color = Color.White, // Azul grisáceo claro
                modifier = Modifier
                    .clickable {
                        navController.navigate(AppScreens.RegisterScreen.route)
                    }
                    .padding(top = 8.dp)
            )

        }

    }
}


@Preview(showBackground = true)
@Composable

fun LoginScreenPreview() {
    Talkit_frontendTheme {
        LoginScreen(navController = rememberNavController())
    }
}


