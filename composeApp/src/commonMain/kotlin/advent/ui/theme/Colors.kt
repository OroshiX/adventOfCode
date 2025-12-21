package advent.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object LightColors {
    val surface = Color(0xFFFFFFFF)
    val text = Color(0xff212121)
    val primary = Color(0xFF57843D)
    val secondary = Color(0xFF5596A1)
    val tertiary = Color(0xFF927115)
    val disabled = Color(0xff333333)
}

object DarkColors {
    val surface = Color(0xFF323232)
    val text = Color(0xFFDCDCDC)
    val primary = Color(0xFFC1C1C1)
    val secondary = Color(0xFF105966)
    val tertiary = Color(0xFFE0BD89)
    val disabled = Color(0xFFCBCBCB)
}

internal val LightColorScheme = lightColorScheme(
    primary = LightColors.primary,
    secondary = LightColors.secondary,
    tertiary = LightColors.tertiary,
    surface = LightColors.surface,
    onSurface = LightColors.text,
    onSecondary = LightColors.text,
)

internal val DarkColorScheme = darkColorScheme(
    primary = DarkColors.primary,
    secondary = DarkColors.secondary,
    tertiary = DarkColors.tertiary,
    surface = DarkColors.surface,
    onSurface = DarkColors.text,
    onSecondary = DarkColors.text,
)