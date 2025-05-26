package com.example.talkit_frontend.ui.screens

import AvatarPicker
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.R
import com.example.talkit_frontend.data.SessionManager
import com.example.talkit_frontend.ui.components.BtAppBar
import com.example.talkit_frontend.ui.components.ProfileTextField
import com.example.talkit_frontend.ui.components.SceneButton
import com.example.talkit_frontend.ui.navigation.AppNavigation
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme

class UpdateProfileActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
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
fun UpdateProfileScreen(navController: NavController) {


    val context = LocalContext.current

    val sessionManager = remember { SessionManager(context) }


    val userName by sessionManager.userName.collectAsState(initial = "Usuario")
    val userEmail by sessionManager.userEmail.collectAsState(initial = "email@example.com")
    val userFechaNac by sessionManager.userFechaNacimiento.collectAsState(initial = "")
    val userAvatar by sessionManager.userAvatar.collectAsState(initial = "")


    var nombre by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(userName, userEmail, userFechaNac, userAvatar) {
        nombre = userName ?: ""
        email = userEmail ?: ""
        avatar = userAvatar ?: ""
    }


    Scaffold(
        bottomBar = {
            BtAppBar(navController)
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4A5C85), // Más claro que #34446C
                            Color(0xFFAEB2CB)  // Base
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(24.dp))

                AvatarPicker(
                    avatarUri = avatarUri,
                    onAvatarClick = {
                        // Lógica para abrir selector de imagen
                    },
                    onAvatarSelected = { uri -> avatarUri = uri }
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
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(4.dp)        // Menor separación entre botones
                        .fillMaxWidth(),       // Altura reducida
                    shape = RoundedCornerShape(8.dp), // Bordes menos redondeados
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF8A80FF)
                    )
                ) {
                    Text(text = "Actualizar", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateProfileScreenPreview() {
    Talkit_frontendTheme {
        UpdateProfileScreen(navController = rememberNavController())
    }
}