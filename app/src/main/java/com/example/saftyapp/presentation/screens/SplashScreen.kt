package com.example.saftyapp.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import com.example.saftyapp.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val scale = remember { Animatable(1f) }

    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) {
        Color(0xFF3E2522)
    } else {
        Color(0xFFFDF3E9)
    }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )

        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )

        delay(500)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.saftyapp_logo_free),
            contentDescription = "Safty App Logo",
            modifier = Modifier
                .scale(scale.value)
                .fillMaxWidth(0.6f)
                .aspectRatio(1f),
            contentScale = ContentScale.Fit
        )
    }
}
