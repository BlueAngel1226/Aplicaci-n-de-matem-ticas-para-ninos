package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FlatDesign2ColorScheme = lightColorScheme(
    primary = FlatIndigo,
    onPrimary = Color.White,
    primaryContainer = FlatIndigoSoft,
    onPrimaryContainer = FlatInkPrimary,
    secondary = FlatCoral,
    onSecondary = Color.White,
    secondaryContainer = FlatCoralSoft,
    onSecondaryContainer = FlatInkPrimary,
    tertiary = FlatMint,
    onTertiary = Color.White,
    background = FlatBackgroundIvory,
    onBackground = FlatInkPrimary,
    surface = FlatSurfaceWhite,
    onSurface = FlatInkPrimary,
    surfaceVariant = FlatBackgroundNeutral,
    onSurfaceVariant = FlatInkSecondary,
    error = FeedbackRedBright,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = FlatDesign2ColorScheme,
        typography = Typography,
        content = content
    )
}
