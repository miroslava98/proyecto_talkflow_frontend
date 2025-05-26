import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.time.LocalDate

interface ApiService {
    @Multipart
    @POST("api/speech/transcribe")
    suspend fun uploadAudio(
        @Part file: MultipartBody.Part,
        @Part("scene") scene: RequestBody? = null,
        @Part("language") language: RequestBody? = null, // Escena seleccionada (ej: "Restaurante")
        @Part("userId") userId: RequestBody? = null // Opcional: ID del usuario
    ): SpeechRecognitionResponse

    @POST("/talkflow/auth/signin")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @POST("talkflow/auth/signup")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @POST("api/chat")
    suspend fun chat(@Body request: ChatRequest): Response<OllamaResponse>

    @POST("/api/tts/generateTTS")
    suspend fun generateSpeech(@Body request: TextToSpeechRequest): Response<TextToSpeechResponse>

    @POST("api/transcriptions")
    suspend fun saveTranscripcion(
        @Body transcripcionDto: TranscripcionDto
    ): Response<Unit>

    @GET("api/transcriptions/listTranscriptions")
    suspend fun getTranscripciones(@Query("userEmail") email: String): Response<List<TranscripcionDto>>

}

// Modelos para la respuestas del backend

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val fecha_nacimiento: LocalDate?,
    val password: String,
    val avatar: String?
)

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(
    val token: String,
    val nombre: String,
    val email: String,
    val avatar: String?,
    val fechaNacimiento: LocalDate

)

data class SpeechRecognitionResponse(
    val recognizedText: String?,
    val status: String?,
    val errorMessage: String?
)

data class Message(val role: String, val content: String)

data class ChatRequest(
    val history: List<Message>,
    val scenePrompt: String,
    val language: String
)

data class OllamaResponse(
    val response: String,
    val audioBase64: String
)

data class TextToSpeechRequest(
    val text: String,
    val language: String
)

data class TextToSpeechResponse(
    val errorMessage: String?,
    val generatedText: String?,
    val audioBase64: String?,

    )

data class TranscripcionDto(
    val idioma: String,
    val textoUser: String,
    val textoChat: String,
    val userEmail: String
)




