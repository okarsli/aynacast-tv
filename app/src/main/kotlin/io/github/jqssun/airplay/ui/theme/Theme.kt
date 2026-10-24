package io.github.jqssun.airplay.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AynaCastColorScheme = darkColorScheme(
    primary = Color(0xFF63E4CF),
    onPrimary = Color(0xFF06211D),
    primaryContainer = Color(0xFF163B39),
    onPrimaryContainer = Color(0xFFB7FFF2),
    secondary = Color(0xFFA69BFF),
    onSecondary = Color(0xFF1B1540),
    secondaryContainer = Color(0xFF302C5B),
    onSecondaryContainer = Color(0xFFE0DCFF),
    tertiary = Color(0xFFF4C979),
    onTertiary = Color(0xFF2E2107),
    background = Color(0xFF080D17),
    onBackground = Color(0xFFF2F5FC),
    surface = Color(0xFF0C1321),
    onSurface = Color(0xFFF2F5FC),
    surfaceVariant = Color(0xFF141F31),
    onSurfaceVariant = Color(0xFFB4C0D3),
    outline = Color(0xFF35445D),
    error = Color(0xFFFF7480),
    onError = Color(0xFF3B0710),
    errorContainer = Color(0xFF5D202B),
    onErrorContainer = Color(0xFFFFD9DC)
)

@Composable
fun AirPlayTheme(content: @Composable () -> Unit) =
    MaterialTheme(colorScheme = AynaCastColorScheme, content = content)
