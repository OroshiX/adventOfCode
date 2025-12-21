package advent.ui.theme

import adventofcode.composeapp.generated.resources.Res
import adventofcode.composeapp.generated.resources.Roboto
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview

private val lineHeightStyle = LineHeightStyle(
    trim = LineHeightStyle.Trim.None,
    alignment = LineHeightStyle.Alignment.Center
)

@Composable
fun AppTypography(): Typography {
    val roboto = FontFamily(
        Font(
            resource = Res.font.Roboto,
            weight = FontWeight.Normal
        )
    )
    return Typography(
        headlineLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
            lineHeightStyle = lineHeightStyle,
            lineHeight = 30.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        headlineSmall = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        displayLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 57.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        displayMedium = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        displaySmall = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        titleLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        titleMedium = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        titleSmall = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        bodyLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        bodyMedium = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        bodySmall = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        labelLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        labelMedium = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        labelSmall = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeightStyle = lineHeightStyle,
        ),
    )
}

@Preview
@Composable
fun AllTypographyPreview() = AdventTheme {
    Column {
        Text("display large", style = MaterialTheme.typography.displayLarge)
        Text("display medium", style = MaterialTheme.typography.displayMedium)
        Text("display small", style = MaterialTheme.typography.displaySmall)
        Text("headline large", style = MaterialTheme.typography.headlineLarge)
        Text("headline medium", style = MaterialTheme.typography.headlineMedium)
        Text("headline small", style = MaterialTheme.typography.headlineSmall)
        Text("title large", style = MaterialTheme.typography.titleLarge)
        Text("title medium", style = MaterialTheme.typography.titleMedium)
        Text("title small", style = MaterialTheme.typography.titleSmall)
        Text("body large", style = MaterialTheme.typography.bodyLarge)
        Text("body medium", style = MaterialTheme.typography.bodyMedium)
        Text("body small", style = MaterialTheme.typography.bodySmall)
        Text("label medium", style = MaterialTheme.typography.labelLarge)
        Text("label medium", style = MaterialTheme.typography.labelMedium)
        Text("label small", style = MaterialTheme.typography.labelSmall)
    }
}