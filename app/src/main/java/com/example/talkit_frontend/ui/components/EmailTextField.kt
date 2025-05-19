package com.example.talkit_frontend.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Correo"
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
        isError = false,
        // Puedes personalizar el borde con el parametro `colors`
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary, // Color cuando está enfocado
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), // Color cuando no está enfocado
            focusedLabelColor = MaterialTheme.colorScheme.primary, // Color del label cuando está enfocado
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) // Color del label cuando no está enfocado
        )

    )
}

