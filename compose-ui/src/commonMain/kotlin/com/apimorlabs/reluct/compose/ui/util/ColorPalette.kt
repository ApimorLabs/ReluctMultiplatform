package com.apimorlabs.reluct.compose.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.kmpalette.DominantColorState
import com.kmpalette.loader.ByteArrayLoader
import com.kmpalette.rememberDominantColorState

@Composable
fun ExtractDominantColor(
    src: ByteArray,
    updateColor: (colorState: DominantColorState<ByteArray>) -> Unit
) {
    val dominantColorState = rememberDominantColorState(
        loader = ByteArrayLoader,
        defaultColor = Color.Gray,
        defaultOnColor = Color.Black
    ) { clearFilters() }
    var image: ImageBitmap? by remember { mutableStateOf(null) }
    LaunchedEffect(src) {
        try {
            image = ByteArrayLoader.load(src)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        dominantColorState.updateFrom(src)
        updateColor(dominantColorState)
    }
}

suspend fun ByteArray.getImageBitmap(): ImageBitmap = ByteArrayLoader.load(this)
