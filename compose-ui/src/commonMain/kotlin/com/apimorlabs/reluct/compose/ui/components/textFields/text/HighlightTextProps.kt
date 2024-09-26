package com.apimorlabs.reluct.compose.ui.components.textFields.text

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
class HighlightTextProps(
    val text: String,
    val url: String? = null,
    val color: Color
)
