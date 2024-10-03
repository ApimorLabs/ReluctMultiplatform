package com.apimorlabs.reluct.util

import androidx.compose.runtime.Composable

@Composable
internal expect fun BackPressHandler(onBackPressed: () -> Unit)
