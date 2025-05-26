import android.app.DatePickerDialog

import android.os.Build
import androidx.annotation.RequiresApi

import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BirthDatePicker(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Estado para abrir/cerrar dialogo
    var showDialog by remember { mutableStateOf(false) }

    // Valores iniciales para el DatePickerDialog
    val calendar = Calendar.getInstance()
    val initialYear = selectedDate?.year ?: calendar.get(Calendar.YEAR)
    val initialMonth = selectedDate?.monthValue?.minus(1) ?: calendar.get(Calendar.MONTH) // 0-based
    val initialDay = selectedDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)

    if (showDialog) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = LocalDate.of(year, month + 1, dayOfMonth)
                onDateSelected(date)
                showDialog = false
            },
            initialYear,
            initialMonth,
            initialDay
        ).show()
    }

    OutlinedButton(
        onClick = { showDialog = true },
        modifier = modifier
    ) {
        Text(text = selectedDate?.format(formatter) ?: "Seleccionar Fecha")
    }
}