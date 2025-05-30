import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.talkit_frontend.R

// Cambia el nombre a MicIconToggle.kt o actualiza el existente
@Composable
fun MicIconToggle(
    isRecording: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled

        ) {
        Icon(
            painter = painterResource(
                id = if (isRecording) R.drawable.mic_on else R.drawable.microphone_lines_slash_solid
            ),
            contentDescription = if (isRecording) "Detener grabación" else "Iniciar grabación",
            tint = if (isRecording) Color.Red else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(25.dp)
        )
    }
}