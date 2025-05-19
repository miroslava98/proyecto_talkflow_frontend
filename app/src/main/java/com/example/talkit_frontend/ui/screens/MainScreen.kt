package com.example.talkit_frontend.ui.screens

import MicIconToggle
import VoiceRecorderViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talkit_frontend.ui.components.BtAppBar
import com.example.talkit_frontend.ui.components.SceneButton
import com.example.talkit_frontend.ui.components.TranscribeTextIcon
import com.example.talkit_frontend.ui.navigation.AppNavigation
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme

import java.io.File


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Talkit_frontendTheme {
                AppNavigation()
            }
        }
        // Dentro de onCreate():
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mainscreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: VoiceRecorderViewModel = viewModel()
    var selectedScene by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("es-ES") }
    var showIcons by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var lastRecordedFile by remember { mutableStateOf<File?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var showTextField by remember { mutableStateOf(false) }

    val idiomas = listOf("es-ES", "en-US", "fr-FR", "de-DE", "it-IT", "bg-BGN")


    val uploadState by viewModel.uploadState.collectAsState()
    when (val state = uploadState) {
        is VoiceRecorderViewModel.UploadState.Success -> {
            LaunchedEffect(state) {
                Toast.makeText(
                    context, "Audio subido: ${
                        state.recognizedText
                    }", Toast.LENGTH_LONG
                ).show()
            }
        }

        is VoiceRecorderViewModel.UploadState.Error -> {
            LaunchedEffect(state) {
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
            }
        }

        else -> {}
    }

    Scaffold(bottomBar = { BtAppBar(navController) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(listOf(Color(0xFFB2A1FF), Color(0xFF8A80FF)))
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Escoge escenario",
                color = Color.White,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            val scenes = listOf("Restaurante", "Aeropuerto", "Peluquería", "Supermercado")
            scenes.forEach { scene ->
                SceneButton(
                    text = scene,
                    onClick = {
                        selectedScene = scene
                        Toast.makeText(context, "Seleccionado: $scene", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Idioma
            Text("Idioma:", color = Color.White)
            androidx.compose.material3.ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                androidx.compose.material3.TextField(
                    readOnly = true,
                    value = selectedLanguage,
                    onValueChange = {},
                    label = { Text("Selecciona idioma") },
                    trailingIcon = {
                        androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    idiomas.forEach { idioma ->
                        DropdownMenuItem(
                            text = { Text(idioma) },
                            onClick = {
                                selectedLanguage = idioma
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            SceneButton(
                text = "Comenzar",
                onClick = {
                    if (selectedScene.isNotEmpty()) {
                        showIcons = true
                        Toast.makeText(context, "Escena: $selectedScene", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Selecciona una escena", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            SceneButton(
                text = "Probar archivo estático",
                onClick = {
                    viewModel.uploadStaticAudio(
                        context = context,
                        scene = selectedScene.ifEmpty { "Prueba" },
                        language = selectedLanguage
                    )
                }
            )

            if (showIcons) {
                Spacer(modifier = Modifier.padding(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 250.dp, start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    MicIconToggle(
                        isRecording = isRecording,
                        onClick = {
                            if (isRecording) {
                                viewModel.stopRecording()
                                lastRecordedFile?.let { file ->
                                    viewModel.uploadAudio(
                                        file = file,
                                        //   scene = selectedScene,
                                        language = selectedLanguage // ← aquí se pasa el idioma seleccionado
                                    )
                                }
                                Toast.makeText(context, "Grabación detenida", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                val outputFile = File(
                                    context.filesDir,
                                    "recording_${System.currentTimeMillis()}.mp3"
                                )
                                lastRecordedFile = outputFile
                                viewModel.startRecording(outputFile)
                                Toast.makeText(context, "Grabando...", Toast.LENGTH_SHORT).show()
                            }
                            isRecording = !isRecording
                        }
                    )
                    //aqui es donde irá el texto transcrito
                    val text = viewModel.recognizedText

                    TranscribeTextIcon(
                        onClick = {
                            if (text.isNotEmpty()) {
                                showTextField = true
                            }
                        }
                    )

                    if (showTextField) {
                        TextField(
                            value = text,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(10.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            readOnly = true
                        )
                    }


                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Talkit_frontendTheme {
        Mainscreen(navController = rememberNavController())
    }
}