package com.example.talkit_frontend.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Correo")
{
    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        label = {Text(label)})
}

