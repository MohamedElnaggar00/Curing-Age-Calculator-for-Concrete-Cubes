package com.example.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = OneUIBlue,
    onPrimary = Color.White,
    primaryContainer = OneUIBlueContainer,
    onPrimaryContainer = Color(0xFF0F3BB0),
    secondary = OneUIAmber,
    onSecondary = Color.White,
    secondaryContainer = OneUIAmberContainer,
    onSecondaryContainer = Color(0xFF92400E),
    tertiary = Color(0xFF10B981),
    tertiaryContainer = Color(0xFFD1FAE5),
    onTertiaryContainer = Color(0xFF065F46),
    background = OneUIGrayBg,
    surface = OneUISurface,
    surfaceVariant = OneUIVariant,
    onSurface = OneUITextDark,
    onSurfaceVariant = OneUITextSubtle
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5B8EFF),
    onPrimary = Color(0xFF0F2A6B),
    primaryContainer = Color(0xFF1B64F2),
    onPrimaryContainer = Color(0xFFE8F0FE),
    secondary = Color(0xFFFBBF24),
    onSecondary = Color(0xFF78350F),
    secondaryContainer = Color(0xFF92400E),
    onSecondaryContainer = Color(0xFFFEF3C7),
    background = Color(0xFF121316),
    surface = Color(0xFF1E2025),
    surfaceVariant = Color(0xFF2B2D33),
    onSurface = Color(0xFFF3F4F8),
    onSurfaceVariant = Color(0xFFA0A5B1)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
