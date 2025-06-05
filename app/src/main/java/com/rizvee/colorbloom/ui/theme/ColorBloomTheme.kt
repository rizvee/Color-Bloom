package com.rizvee.colorbloom.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color // Required for custom colors if not defined in Color.kt

// Placeholder for actual colors, assuming they will be in Color.kt
// If Color.kt is not created yet, these would cause errors,
// but they are needed for the theme structure.
// val Purple40 = Color(0xFF6650a4) // Example, will be in Color.kt
// val PurpleGrey40 = Color(0xFF625b71) // Example, will be in Color.kt
// val Pink40 = Color(0xFF7D5260) // Example, will be in Color.kt
// val Purple80 = Color(0xFFD0BCFF) // Example, will be in Color.kt
// val PurpleGrey80 = Color(0xFFCCC2DC) // Example, will be in Color.kt
// val Pink80 = Color(0xFFEFB8C8) // Example, will be in Color.kt


private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

@Composable
fun ColorBloomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // You'll create a Typography.kt
        content = content
    )
}
