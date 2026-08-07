package com.example.dentalaccounting.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0F4C81),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE2EDF8),
    onPrimaryContainer = Color(0xFF001E3C),
    secondary = Color(0xFF00A896),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCF2ED),
    onSecondaryContainer = Color(0xFF003831),
    tertiary = Color(0xFF028090),
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF64B5F6),
    onPrimary = Color(0xFF002F5D),
    primaryContainer = Color(0xFF004684),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = Color(0xFF4DB6AC),
    onSecondary = Color(0xFF003732),
    secondaryContainer = Color(0xFF004F48),
    onSecondaryContainer = Color(0xFF80E2D8),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9)
)

@Composable
fun DentalAccountingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
