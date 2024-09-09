package com.apimorlabs.reluct.compose.ui.components.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.ImageBitmap
import com.apimorlabs.reluct.common.models.domain.appInfo.Icon
import com.apimorlabs.reluct.compose.ui.util.getImageBitmap

@Composable
fun GetImageBitmap(src: Icon, updatePainter: (ImageBitmap) -> Unit) {
    LaunchedEffect(src.icon) {
        updatePainter(src.icon.getImageBitmap())
    }
}
