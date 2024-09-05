package com.apimorlabs.reluct.compose.ui.util

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
internal actual fun canShowDynamic(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@RequiresApi(Build.VERSION_CODES.S)
@Composable
internal actual fun dynamicLightScheme(): ColorScheme =
    dynamicLightColorScheme(LocalContext.current)

@RequiresApi(Build.VERSION_CODES.S)
@Composable
internal actual fun dynamicDarkScheme(): ColorScheme = dynamicDarkColorScheme(LocalContext.current)