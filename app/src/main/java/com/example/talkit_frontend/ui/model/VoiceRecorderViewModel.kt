import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkit_frontend.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class VoiceRecorderViewModel : ViewModel() {
    private var mediaRecorder: android.media.MediaRecorder? = null
    private val apiService = RetrofitClient.apiService

    // Estados para la UI
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    sealed class UploadState {
        object Idle : UploadState()
        object Loading : UploadState()
        data class Success(val recognizedText: String) : UploadState()
        data class Error(val message: String) : UploadState()
    }

    fun startRecording(outputFile: File) {
        viewModelScope.launch {
            mediaRecorder = android.media.MediaRecorder().apply {
                setAudioSource(android.media.MediaRecorder.AudioSource.MIC)
                setOutputFormat(android.media.MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(android.media.MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile.absolutePath)
                prepare()
                start()
            }
        }
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    // Nueva función para subir el audio
    fun uploadAudio(file: File, userId: String? = null, language: String) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            try {
                val requestFile = file.asRequestBody("audio/wav".toMediaTypeOrNull())
                val audioPart = MultipartBody.Part.createFormData(
                    "file",  // Nombre del campo en el backend
                    file.name,
                    requestFile
                )
                //val scenePart = scene.toRequestBody("text/plain".toMediaTypeOrNull())
                val userIdPart = userId?.toRequestBody("text/plain".toMediaTypeOrNull())
                val languagePart = language.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiService.uploadAudio(audioPart, languagePart)
                if (response != null) {
                    if (response.status == "SUCCESS") {
                        _uploadState.value =
                            UploadState.Success(response.recognizedText ?: "Sin texto")
                    } else {
                        _uploadState.value =
                            UploadState.Error(response.errorMessage ?: "Error desconocido")

                    }
                }
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error("Error de red: ${e.message}")
                Log.e("ERROR", "${e.message}")
            }
        }
    }


    fun uploadStaticAudio(context: Context, scene: String, language: String) {
        val inputStream = context.resources.openRawResource(R.raw.harvard)
        val tempFile = File(context.cacheDir, "harvard.wav")
        Log.d("UPLOAD", "File exists: ${tempFile.exists()}, size: ${tempFile.length()}")


        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        viewModelScope.launch {
            uploadAudio(
                file = tempFile,
                language = language
            )
        }
    }

}