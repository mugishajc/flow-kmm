package com.mugishajc.flowkmm.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Blue = Color(0xFF4A90D9)
private val DarkBlue = Color(0xFF2C5F8A)
private val Surface = Color(0xFFF8F9FA)
private val DarkSurface = Color(0xFF1A1A2E)

private val LightColors = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    surface = Surface,
    background = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    surface = DarkSurface,
    background = Color(0xFF0F0F23),
)

@Composable
fun FlowKmmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
