package com.example.saftyapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Orange80,
    onPrimary = Color.Black,
    secondary = OrangeGrey80,
    tertiary = Peach80,

    background = DarkBackground,
    onBackground = OnDark,

    surface = DarkSurface,
    onSurface = OnDark,

    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnDark
)

private val LightColorScheme = lightColorScheme(
    primary = Orange40,
    onPrimary = OnPrimary,
    secondary = OrangeGrey40,
    tertiary = Peach40,

    background = LightBackground,
    onBackground = OnBackground,

    surface = LightSurface,
    onSurface = OnSurface,

    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimary
)

@Composable
fun SaftyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}