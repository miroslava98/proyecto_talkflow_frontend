import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("api/speech/transcribe") // Ajusta la ruta según tu backend
    suspend fun uploadAudio(
        @Part file: MultipartBody.Part,
        @Part("language") language: RequestBody, // Escena seleccionada (ej: "Restaurante")
        @Part("userId") userId: RequestBody? = null // Opcional: ID del usuario
    ): SpeechRecognitionResponse // Define "ApiResponse" según la respuesta de tu backend

    @POST("/talkflow/auth/signin")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

}

data class RegisterRequest(val username: String, val email: String, val password: String)


data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val token: String, val nombre: String)




// Modelo para la respuesta del backend
data class SpeechRecognitionResponse(
    val recognizedText: String?,
    val status: String?,
    val errorMessage: String? // URL del audio subido (opcional)
)

