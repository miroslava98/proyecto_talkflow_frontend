package com.example.talkit_frontend.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.ui.components.BtAppBar
import com.example.talkit_frontend.ui.navigation.AppNavigation
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme

class RecordsActivity : ComponentActivity() {
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
fun RecordsScreen(navController: NavController) {
    val sampleTranscriptions = listOf(
        "Restaurante - 02/05/2025 - 'Hola, ¿tienen mesa para dos?'",
        "Aeropuerto - 01/05/2025 - '¿Dónde está la puerta de embarque?'",
        "Supermercado - 30/04/2025 - '¿Cuánto cuesta esta manzana?'",
        "Peluquería - 29/04/2025 - 'Quiero un corte moderno, por favor.'"
    )

    Scaffold(
        bottomBar = {
            BtAppBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Historial de transcripciones",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sampleTranscriptions) { transcription ->
                    Card(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)

                    ) {
                        Text(
                            text = transcription,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 16.sp,
                            color = Color.Black
                        )
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