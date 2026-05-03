package com.example.multiplicationzoo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary    = SkyBlue,
    secondary  = SunshineYellow,
    tertiary   = GrassGreen,
    background = BackgroundColor,
    surface    = SurfaceColor
)

@Composable
fun MultiplicationZooTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography  = Typography,
        content     = content
    )
}
