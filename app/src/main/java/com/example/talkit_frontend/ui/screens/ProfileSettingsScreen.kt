package com.example.talkit_frontend.ui.screens

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.R
import com.example.talkit_frontend.data.SessionManager
import com.example.talkit_frontend.ui.components.BtAppBar
import com.example.talkit_frontend.ui.components.ConfirmationButton
import com.example.talkit_frontend.ui.components.SceneButton
import com.example.talkit_frontend.ui.navigation.AppNavigation
import com.example.talkit_frontend.ui.navigation.AppScreens
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ProfileSettingsActivity : ComponentActivity() {
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
fun ProfileSettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }


    val userName by sessionManager.userName.collectAsState(initial = "Usuario")
    val userEmail by sessionManager.userEmail.collectAsState(initial = "email@example.com")



    val scrollState = rememberScrollState() // Scroll state para el scroll vertical


    Scaffold(
        bottomBar = { BtAppBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4A5C85), // Más claro que #34446C
                            Color(0xFFAEB2CB)  // Base
                        )
                    )
                )
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Limitar ancho máximo y centrar contenido
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Row con imagen y perfil lado a lado
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_prueba),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    // El ProfileSection se expande para llenar el espacio restante
                    ProfileSection(modifier = Modifier.weight(1f), userName!!, userEmail!!)
                }

                Text(
                    text = "Ajustes",
                    color = Color.White,
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                SettingsGroup(title = "General") {
                    SettingsItem(
                        "Modificar perfil",
                        "Personaliza tu perfil",
                        onClick = {
                            navController.navigate(
                                AppScreens.UpdateProfileScreen.route
                            )
                        })
                    SettingsItem("Idioma", "Cambia idioma")
                    SettingsItem("Tema", "Cambia el tema de la app")
                }

                Spacer(modifier = Modifier.height(16.dp))

                SettingsGroup(title = "Soporte") {
                    SettingsItem("Contacto")
                    SettingsItem("Feedback")
                    SettingsItem("Política de Privacidad")
                }

                Spacer(modifier = Modifier.height(24.dp))

                val coroutineScope = rememberCoroutineScope()

                ConfirmationButton("Cerrar sesión", onClick = {
                    coroutineScope.launch {
                        val sessionManager = SessionManager(context)
                        sessionManager.clearToken()
                        navController.navigate(AppScreens.LoginScreen.route) {
                            popUpTo(0)
                        }
                    }
                })
            }
        }
    }
}

@Composable
fun ProfileSection(modifier: Modifier = Modifier, userName: String, userEmail: String) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = userName,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = userEmail,
            color = Color.LightGray,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            content()
        }
    }
}

@Composable
fun SettingsItem(title: String, subtitle: String = "", onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
            .padding(12.dp)
            .padding(vertical = 8.dp)
    ) {

        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                color = Color.LightGray,
                fontSize = 12.sp
            )
        }
    }


}


@Preview(showBackground = true)
@Composable
fun ProfileSettingsScreen() {
    Talkit_frontendTheme {
        ProfileSettingsScreen(navController = rememberNavController())
    }
}

