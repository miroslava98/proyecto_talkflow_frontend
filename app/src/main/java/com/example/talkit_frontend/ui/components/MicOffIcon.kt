package com.example.talkit_frontend.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import com.example.talkit_frontend.R

@Composable
fun MicOffIcon(onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = R.drawable.microphone_lines_slash_solid),
        contentDescription = "Micrófono apagado",
        tint = Color.White, // Cambia el color si lo deseas
        modifier = Modifier
            .size(48.dp)
            .clickable(onClick = onClick)
    )
}
