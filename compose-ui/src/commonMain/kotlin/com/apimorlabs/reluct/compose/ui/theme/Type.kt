package com.apimorlabs.reluct.compose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.lexenddeca_bold
import com.apimorlabs.reluct.compose.ui.lexenddeca_light
import com.apimorlabs.reluct.compose.ui.lexenddeca_medium
import com.apimorlabs.reluct.compose.ui.lexenddeca_regular
import com.apimorlabs.reluct.compose.ui.lexenddeca_thin
import org.jetbrains.compose.resources.Font

@Composable
private fun myFonts() = FontFamily(
    Font(Res.font.lexenddeca_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(Res.font.lexenddeca_light, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(Res.font.lexenddeca_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(Res.font.lexenddeca_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(Res.font.lexenddeca_thin, weight = FontWeight.Thin, style = FontStyle.Normal)
)

// Set of Material typography styles to start with
@Composable
internal fun customTypography() = Typography(
    headlineLarge = TextStyle(
        fontFamily = myFonts(),
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 38.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = myFonts(),
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = myFonts(),
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 30.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = myFonts(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = myFonts(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    titleLarge = TextStyle(
        fontFamily = myFonts(),
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    titleMedium = TextStyle(
        fontFamily = myFonts(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = myFonts(),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = myFonts(),
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = myFonts(),
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 6.sp
    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
     */
)
