package com.example.talkit_frontend.ui.screens

import ChatRequest
import Message
import MicIconToggle
import OllamaResponse
import RetrofitClient
import SpeechRecognitionResponse
import TranscripcionDto
import VoiceRecorderViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.talkit_frontend.R
import com.example.talkit_frontend.data.SessionManager
import com.example.talkit_frontend.ui.components.ConfirmationButton
import com.example.talkit_frontend.ui.navigation.AppNavigation
import com.example.talkit_frontend.ui.theme.Talkit_frontendTheme
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.internal.userAgent
import java.io.File

class ChatScreenActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

data class AssistantMessage(val text: String, val audioBase64: String?)

fun playBase64Audio(
    base64: String,
    context: android.content.Context,
    onComplete: () -> Unit
): Pair<MediaPlayer, File>? {
    return try {
        val audioBytes = Base64.decode(base64, Base64.DEFAULT)
        val tempFile = File.createTempFile("audio", ".mp3", context.cacheDir)
        tempFile.writeBytes(audioBytes)

        val mediaPlayer = MediaPlayer().apply {
            setDataSource(tempFile.absolutePath)
            setOnPreparedListener { it.start() }
            setOnCompletionListener {
                onComplete()
                it.release()
                tempFile.delete()
            }
            setOnErrorListener { mp, _, _ ->
                mp.release()
                tempFile.delete()
                Toast.makeText(context, "Error reproduciendo audio", Toast.LENGTH_SHORT).show()
                true
            }
            prepareAsync()
        }
        mediaPlayer to tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al reproducir audio", Toast.LENGTH_SHORT).show()
        null
    }
}


@Composable
fun ChatScreen(
    scene: String,
    chatLanguage: String,
    userLanguage: String,
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: VoiceRecorderViewModel = viewModel()
    var isRecording by remember { mutableStateOf(false) }
    var lastRecordedFile by remember { mutableStateOf<File?>(null) }
    var inputText by remember { mutableStateOf("") }
    var showTranscription by remember { mutableStateOf(false) }
    var messages by remember { mutableStateOf(listOf<Any>()) }
    var chosenScene = scene.lowercase()
    var isLoading by remember { mutableStateOf(false) }
    val playingStates = remember { mutableStateMapOf<Int, Boolean>() }
    val sessionManager = remember { SessionManager(context) }


    var currentMediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentTempFile by remember { mutableStateOf<File?>(null) }
    var currentPlayingIndex by remember { mutableStateOf<Int?>(null) }

    val coroutineScope = rememberCoroutineScope()


    val uploadState by viewModel.uploadState.collectAsState()

    LaunchedEffect(uploadState) {
        when (val state = uploadState) {
            is VoiceRecorderViewModel.UploadState.Success -> {

                inputText = state.recognizedText
                showTranscription = true
            }

            is VoiceRecorderViewModel.UploadState.Error -> {
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
            }

            else -> {}
        }


    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.95f))
                    .padding(16.dp)

            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(
                        "Escena: $scene",
                        "Idioma: $chatLanguage",
                        "Voz: $userLanguage"
                    ).forEach { label ->
                        Box(
                            modifier = Modifier
                                .background(
                                    Color.White.copy(alpha = 0.15f),
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )
                        }
                    }
                }
            }
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
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        )
                    )
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween

        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .padding(vertical = 8.dp)
            ) {
                items(messages.size) { index ->
                    val message = messages[index]
                    when (message) {
                        is String -> {
                            val isUser = message.startsWith("\uD83E\uDDD1")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = if (isUser) androidx.compose.foundation.layout.Arrangement.End else androidx.compose.foundation.layout.Arrangement.Start
                            ) {
                                Text(
                                    text = message,
                                    modifier = Modifier
                                        .background(
                                            if (isUser) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                            else MaterialTheme.colorScheme.surfaceVariant,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        is AssistantMessage -> {
                            val isPlaying = playingStates[index] ?: false
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "🤖 Asistente: ${message.text}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    message.audioBase64?.let { audio ->
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = {
                                                if (isPlaying) {
                                                    // Si está reproduciendo, pausar y limpiar
                                                    currentMediaPlayer?.let {
                                                        if (it.isPlaying) it.pause()
                                                        else it.start()
                                                    }
                                                } else {
                                                    // Parar audio anterior
                                                    currentMediaPlayer?.release()
                                                    currentTempFile?.delete()
                                                    currentMediaPlayer = null
                                                    currentTempFile = null
                                                    currentPlayingIndex = null

                                                    // Iniciar nuevo audio
                                                    val pair = playBase64Audio(audio, context) {
                                                        currentPlayingIndex = null
                                                    }
                                                    pair?.let { (mp, file) ->
                                                        currentMediaPlayer = mp
                                                        currentTempFile = file
                                                        currentPlayingIndex = index
                                                    }
                                                }
                                            }
                                        ) {
                                            Text(
                                                if (isPlaying && currentMediaPlayer?.isPlaying == true) "⏸️ Pausar"
                                                else if (isPlaying) "▶️ Reanudar"
                                                else "🔊 Escuchar"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {

                // Mostrar input de texto
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .heightIn(min = 56.dp),
                    placeholder = { Text("Escribe tu mensaje") },
                    enabled = !(isRecording || isLoading)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 🎙️ Grabación de voz
                    MicIconToggle(
                        isRecording = isRecording,
                        onClick = {
                            if (isLoading) return@MicIconToggle
                            coroutineScope.launch {
                                if (isRecording) {
                                    viewModel.stopRecording()
                                    lastRecordedFile?.let { file ->
                                        viewModel.uploadAudio(
                                            file,
                                            scene = scene,
                                            language = userLanguage
                                        )
                                    }
                                    Toast.makeText(
                                        context,
                                        "Grabación detenida",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    //INICIAR GRABACIÓN
                                    val outputFile = File(
                                        context.filesDir,
                                        "recording_${System.currentTimeMillis()}.mp4"
                                    )
                                    lastRecordedFile = outputFile
                                    viewModel.startRecording(outputFile)
                                    Toast.makeText(
                                        context,
                                        "Grabando...",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                isRecording = !isRecording
                            }
                        },
                        enabled = !isLoading
                    )


                    // ✉️ Send button
                    ConfirmationButton(
                        text = "Enviar",
                        enabled = !(isRecording || isLoading),
                        onClick = {
                            coroutineScope.launch {
                                if (inputText.isNotBlank()) {
                                    isLoading = true
                                    val truncatedInput = inputText.take(500)
                                    val userMessage = "🧑 Tú: $truncatedInput"

                                    val historyMessages = messages.mapNotNull {
                                        when (it) {
                                            is String -> {
                                                when {
                                                    it.startsWith("🧑") -> Message(
                                                        "user",
                                                        it.removePrefix("🧑 Tú: ")
                                                    )

                                                    it.startsWith("🤖") -> Message(
                                                        "assistant",
                                                        it.removePrefix("🤖 Asistente: ")
                                                    )

                                                    else -> null
                                                }
                                            }

                                            is AssistantMessage -> Message("assistant", it.text)
                                            else -> null
                                        }
                                    }

                                    val request = ChatRequest(
                                        scenePrompt = chosenScene,
                                        language = chatLanguage,
                                        history = historyMessages + Message("user", inputText)
                                    )

                                    try {
                                        val response = RetrofitClient.apiService.chat(request)
                                        if (response.isSuccessful) {
                                            val body = response.body()
                                            val reply = body?.response ?: "Respuesta vacía"
                                            val audio = body?.audioBase64
                                            if (audio != null && audio.matches(Regex("^[A-Za-z0-9+/=\\r\\n]+$"))) {

                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Audio no válido para reproducir",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            messages = messages + listOf(
                                                userMessage,
                                                AssistantMessage(reply, audio)
                                            )


                                            val token = sessionManager.fetchAuthToken()
                                            val email = sessionManager.userEmail.firstOrNull()
                                            if (email != null) {
                                                val transcripcion = TranscripcionDto(
                                                    idioma = chatLanguage,
                                                    textoUser = inputText,
                                                    textoChat = reply,
                                                    userEmail = email
                                                )

                                                coroutineScope.launch {
                                                    try {
                                                        val saveResponse =
                                                            RetrofitClient.apiService.saveTranscripcion(
                                                                transcripcion
                                                            )
                                                        if (!saveResponse.isSuccessful) {
                                                            Log.e(
                                                                "ChatScreen",
                                                                "Error guardando transcripción: ${saveResponse.code()}"
                                                            )
                                                        }
                                                    } catch (e: Exception) {
                                                        Log.e(
                                                            "ChatScreen",
                                                            "Excepción guardando transcripción: ${e.message}"
                                                        )
                                                    }
                                                }
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Error: ${response.code()}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Excepción: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } finally {
                                        isLoading = false
                                        inputText = ""
                                    }
                                }
                            }
                        }
                    )
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    Talkit_frontendTheme {
        ChatScreen(
            scene = "",
            chatLanguage = "",
            userLanguage = "",
            navController = rememberNavController()
        )
    }
}