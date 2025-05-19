package com.example.talkit_frontend.ui.screens

import LoginRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.ui.components.EmailTextField
import com.example.talkit_frontend.ui.components.PasswordTextField
import com.example.talkit_frontend.ui.navigation.AppNavigation
import com.example.talkit_frontend.ui.navigation.AppScreens
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
//añadir el navControler a cada Screen para poder navegar
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var correo by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // Para mostrar un loading si la solicitud está en proceso
    var errorMessage by remember { mutableStateOf("") } // Mensaje de error si las credenciales son incorrectas


    val handleLogin = {
        if (correo.isNotEmpty() && contrasenya.isNotEmpty()) {
            isLoading = true
            val loginRequest = LoginRequest(correo, contrasenya)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitClient.apiService.loginUser(loginRequest)
                    if (response.isSuccessful && response.body() != null) {
                        val userResponse = response.body()!!
                        // Aquí el token si es necesario,
                        val token = userResponse.token
                        navController.navigate(AppScreens.MainScreen.route)
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
        PasswordTextField(
            value = contrasenya,
            onValueChange = { contrasenya = it },
            label = "Contraseña"
        )
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(),
        )
        FilledTonalButton(
            onClick = {
                navController.navigate(AppScreens.MainScreen.route)
            }
        ) {
            Text("Entrar")
        }

        Text(
            text = "¿No tienes una cuenta? Regístrate aquí",
            color = Color.Cyan,
            modifier = Modifier
                .clickable {
                    navController.navigate(AppScreens.RegisterScreen.route)
                }
                .padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Talkit_frontendTheme {
        LoginScreen(navController = rememberNavController())

    }
}