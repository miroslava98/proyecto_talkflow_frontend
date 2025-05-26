import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BirthDatePicker(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val isInEditMode = LocalView.current.isInEditMode
    val activity = if (!isInEditMode) context as? AppCompatActivity else null

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    OutlinedButton(
        onClick = {
            activity?.let {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecciona tu fecha de nacimiento")
                    .build()

                datePicker.show(it.supportFragmentManager, "DATE_PICKER")

                datePicker.addOnPositiveButtonClickListener { millis ->
                    val localDate = Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onDateSelected(localDate)
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = selectedDate?.format(formatter) ?: "Seleccionar Fecha")
    }
}
