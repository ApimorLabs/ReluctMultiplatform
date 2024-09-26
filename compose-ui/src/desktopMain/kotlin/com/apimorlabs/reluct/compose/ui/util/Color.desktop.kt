package com.apimorlabs.reluct.compose.ui.util

import androidx.compose.ui.graphics.Color

/**
 * Returns ARGB color value
 */
actual fun Color.toLegacyInt(): Int {
    return org.jetbrains.skia.Color.makeARGB(
        (alpha * 255.0f + 0.5f).toInt(),
        (red * 255.0f + 0.5f).toInt(),
        (green * 255.0f + 0.5f).toInt(),
        (blue * 255.0f + 0.5f).toInt()
    )
}
