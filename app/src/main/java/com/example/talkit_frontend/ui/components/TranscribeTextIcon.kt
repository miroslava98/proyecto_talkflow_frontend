package com.example.talkit_frontend.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.talkit_frontend.R

@Composable
fun TranscribeTextIcon(onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = R.drawable.language_solid),
        contentDescription = "Transcribe texto",
        tint = Color.White, // Cambia el color si lo deseas
        modifier = Modifier
            .size(48.dp)
            .clickable(onClick = onClick)
    )
}