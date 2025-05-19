package com.example.talkit_frontend.ui.screens

import BirthDatePicker
import RegisterRequest
import RetrofitClient
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.R
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

@Composable
fun RegisterScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var fecha_nacimiento by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }
    var confirmar_contrasenya by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val handleRegister = {
        if (nombre.isNotEmpty() && correo.isNotEmpty() && fecha_nacimiento != null && contrasenya.isNotEmpty() && confirmar_contrasenya.isNotEmpty()) {
            fecha_nacimiento?.let { date ->

                val registerRequest =
                    RegisterRequest(nombre, correo, fecha_nacimiento, contrasenya, avatar)

                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        val response = RetrofitClient.apiService.register(registerRequest)
                        if (response.isSuccessful && response.body() != null) {
                            val result = response.body()
                            withContext(Dispatchers.Main) {
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

        BirthDatePicker(
            selectedDate = fecha_nacimiento,
            onDateSelected = { fecha_nacimiento = it }

        )

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
        RegisterScreen(navController = rememberNavController())
    }
}