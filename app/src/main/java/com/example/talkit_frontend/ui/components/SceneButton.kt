package com.example.talkit_frontend.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SceneButton(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {

    val backgroundColor = if (isSelected) Color(0xFF8A80FF) else Color.White.copy(alpha = 0.1f)
    val textColor = if (isSelected) Color.White else Color.LightGray
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)        // Menor separación entre botones
            .width(180.dp)        // Ancho reducido
            .height(50.dp),       // Altura reducida
        shape = RoundedCornerShape(8.dp), // Bordes menos redondeados
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}


