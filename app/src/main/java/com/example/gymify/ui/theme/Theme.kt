package com.example.gymify.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Light Theme Colors
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = TextLight,
    secondary = PurpleGrey40,
    onSecondary = TextLight,
    tertiary = Pink40,
    onTertiary = TextLight,
    background = BackgroundLight,
    surface = PurpleGrey80,
    onBackground = TextDark,
    onSurface = TextDark,
)

// Dark Theme Colors
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = TextLight,
    secondary = PurpleGrey80,
    onSecondary = TextLight,
    tertiary = Pink80,
    onTertiary = TextLight,
    background = BackgroundDark,
    surface = PurpleGrey80,
    onBackground = TextLight,
    onSurface = TextLight,
)

@Composable
fun GymifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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