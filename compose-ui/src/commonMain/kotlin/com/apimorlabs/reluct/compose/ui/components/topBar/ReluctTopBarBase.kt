package com.apimorlabs.reluct.compose.ui.components.topBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.SRes
import com.apimorlabs.reluct.compose.ui.profile_picture
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.github.panpf.sketch.rememberAsyncImagePainter
import com.github.panpf.sketch.request.ComposableImageRequest
import org.jetbrains.compose.resources.stringResource
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.images.rememberAsyncImage
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.theme.Shapes

@Composable
fun ProfilePicture(
    pictureUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    onPictureClicked: () -> Unit = { },
) {
    var imageLoading by remember {
        mutableStateOf(true)
    }

    val painter = pictureUrl?.let {
        rememberAsyncImagePainter(
            request = ComposableImageRequest(uri = it) {
                placeholder()
            }
        )
        rememberAsyncImage(
            url = it,
            onStartLoading = { imageLoading = true },
            onFinishLoading = { imageLoading = false }
        )
    }

    IconButton(
        modifier = modifier.size(size),
        onClick = onPictureClicked
    ) {
        if (painter == null) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(Res.string.profile_picture),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(size)
            )
        } else {
            Image(
                modifier = Modifier
                    .clip(Shapes.large)
                    .fillMaxSize(),
                painter = painter,
                contentDescription = stringResource(Res.string.profile_picture)
            )
        }
    }
}