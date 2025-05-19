import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun BirthDatePicker(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val isInEditMode = LocalView.current.isInEditMode
    val activity = if (!isInEditMode) context as? AppCompatActivity else null
    Button(
        onClick = {
            activity?.let {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecciona tu fecha de nacimiento")
                    .build()

                datePicker.show(it.supportFragmentManager, "DATE_PICKER")

                datePicker.addOnPositiveButtonClickListener { millis ->
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = millis
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = formatter.format(calendar.time)
                    onDateSelected(formattedDate)
                }
            }
        }
    ) {
        Text(text = if (selectedDate.isEmpty()) "Seleccionar Fecha" else selectedDate)
    }
}


