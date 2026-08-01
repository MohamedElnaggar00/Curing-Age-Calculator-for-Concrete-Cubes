package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = OneUIPrimary,
    secondary = OneUIPrimary,
    tertiary = OneUIPrimary,
    background = OneUIDarkBackground,
    surface = OneUIDarkSurface,
    surfaceVariant = OneUIDarkSurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = OneUITextPrimary,
    onSurface = OneUITextPrimary,
    onSurfaceVariant = OneUITextSecondary,
    outline = OneUIDivider,
    error = OneUIError,
    errorContainer = OneUIError.copy(alpha = 0.2f),
    onErrorContainer = OneUIError
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Always dark
    dynamicColor: Boolean = false, // Disable dynamic to strictly match One UI
    content: @Composable () -> Unit,
) {
    val colorScheme = DarkColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
