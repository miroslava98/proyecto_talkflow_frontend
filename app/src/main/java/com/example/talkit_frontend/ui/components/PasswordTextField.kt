package com.example.talkit_frontend.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable

fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Contraseña"
) {
    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        label = { Text(label) })
}