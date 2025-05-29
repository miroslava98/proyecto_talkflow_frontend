package com.example.talkit_frontend.ui.screens

import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color

import androidx.navigation.NavController
import com.example.talkit_frontend.ui.navigation.AppScreens
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import com.example.talkit_frontend.R

@Composable
fun AnimatedSplashScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var stage by remember { mutableStateOf(0) }

    val messages =
        listOf("Welcome", "Bienvenido", "Willkommen", "Bienvenue", "Benvenuto", "ようこそ", "환영합니다")
    var currentMessageIndex by remember { mutableStateOf(0) }

    // Cambio de estados con delay
    LaunchedEffect(true) {
        delay(1000)
        stage = 1
        delay(1000)
        stage = 2
        // cambio de mensajes
        for (i in 1..messages.size) {
            delay(500)
            currentMessageIndex = i % messages.size
        }
        delay(800)
        stage = 3
        delay(1200)
        navController.navigate(AppScreens.MainScreen.route) {
            popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (stage) {
            0 -> InitialHalfScreen()
            1 -> DiagonalTransitionEffect()
            2 -> WhiteMessageScreen(messages[currentMessageIndex])
            3 -> LogoScreen()
        }
    }
}

@Composable
fun InitialHalfScreen() {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF34446C))
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF4A5C85))
        )
    }
}

@Composable
fun DiagonalTransitionEffect() {
    // Transición falsa: simple reemplazo diagonal
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = Color.White)
        drawPath(
            path = Path().apply {
                moveTo(0f, size.height)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                close()
            },
            color = Color(0xFF34446C)
        )
        drawPath(
            path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(0f, size.height)
                close()
            },
            color = Color(0xFF4A5C85)
        )
    }
}


@Composable
fun WhiteMessageScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color(0xFF2C3B5C)
            )
        )
    }
}

@Composable
fun LogoScreen() {
    var visible by remember { mutableStateOf(false) }
    // Animación de opacidad (fade-in)
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "LogoAlpha"
    )


    // Animación de escala
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1000, easing = EaseOutBack),
        label = "LogoScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.launcher), // Usa el nombre correcto de tu recurso
            contentDescription = "Logo TalkFlow",
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
        )
    }
}

