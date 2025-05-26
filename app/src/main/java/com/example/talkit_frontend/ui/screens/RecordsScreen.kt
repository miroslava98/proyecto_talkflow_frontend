package com.example.talkit_frontend.ui.screens

import TranscripcionDto
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.data.SessionManager
import com.example.talkit_frontend.ui.components.BtAppBar
import com.example.talkit_frontend.ui.navigation.AppNavigation
import com.example.talkit_frontend.ui.navigation.AppScreens
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class RecordsActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(navController: NavController) {
    val sessionManager = SessionManager(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    val transcripciones = remember { mutableStateListOf<TranscripcionDto>() }

    LaunchedEffect(Unit) {
        val email = sessionManager.userEmail.firstOrNull()
        if (email != null) {
            coroutineScope.launch {
                try {
                    val response = RetrofitClient.apiService.getTranscripciones(email)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            transcripciones.clear()
                            transcripciones.addAll(it)
                        }
                    } else {
                        Log.e("Historial", "Error: ${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("Historial", "Excepción: ${e.message}")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.95f))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Historial de transcripciones",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                )
            }
        },
        bottomBar = {
            BtAppBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                        )
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (transcripciones.isEmpty()) {
                Text(
                    text = "No hay transcripciones disponibles.",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(transcripciones) { t ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "🌍 Idioma: ${t.idioma}",
                                    style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "🧑 Tú: ${t.textoUser}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF333333)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "🤖 Asistente: ${t.textoChat}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecordsScreenPreview() {
    Talkit_frontendTheme {
        RecordsScreen(navController = rememberNavController())
    }
}