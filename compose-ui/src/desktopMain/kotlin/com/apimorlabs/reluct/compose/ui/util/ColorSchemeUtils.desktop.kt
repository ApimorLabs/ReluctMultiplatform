package com.apimorlabs.reluct.compose.ui.util

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

internal actual fun canShowDynamic(): Boolean = false

@Composable
internal actual fun dynamicLightScheme(): ColorScheme = lightColorScheme()

@Composable
internal actual fun dynamicDarkScheme(): ColorScheme = darkColorScheme()
