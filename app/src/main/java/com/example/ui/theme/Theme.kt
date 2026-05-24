package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = DeepMaroon,
    secondary = RoyalSaffron,
    tertiary = AntiqueGold,
    background = LotusCream,
    surface = LotusCream,
    onPrimary = CreamWhite,
    onSecondary = CreamWhite,
    onTertiary = PrimaryText,
    onBackground = PrimaryText,
    onSurface = PrimaryText
)

private val DarkColorScheme = darkColorScheme(
    primary = RoyalSaffron,
    secondary = AntiqueGold,
    tertiary = DeepMaroon,
    background = MidnightIndigo,
    surface = MidnightIndigo,
    onPrimary = PrimaryText,
    onSecondary = PrimaryText,
    onTertiary = CreamWhite,
    onBackground = CreamWhite,
    onSurface = CreamWhite
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep saffron color brand-focused instead of system blue/green
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
