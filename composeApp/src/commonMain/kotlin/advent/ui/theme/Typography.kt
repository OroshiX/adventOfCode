package advent.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

private val lineHeightStyle = LineHeightStyle(
    trim = LineHeightStyle.Trim.None,
    alignment = LineHeightStyle.Alignment.Center
)

@Composable
fun AppTypography(): Typography {
    return Typography(
        headlineLarge = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeightStyle = lineHeightStyle,
            lineHeight = 30.sp,
        ),
        headlineMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeightStyle = lineHeightStyle,
        ),
        displaySmall = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeightStyle = lineHeightStyle,
        )
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