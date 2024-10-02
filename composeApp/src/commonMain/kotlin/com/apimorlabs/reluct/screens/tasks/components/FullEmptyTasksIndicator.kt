package com.apimorlabs.reluct.screens.tasks.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.images.ImageWithDescription
import com.apimorlabs.reluct.compose.ui.empty
import com.apimorlabs.reluct.compose.ui.no_tasks_text
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun FullEmptyTasksIndicator(
    showAnimationProvider: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    val showAnimation = remember { derivedStateOf(showAnimationProvider) }
    AnimatedVisibility(
        visible = showAnimation.value,
        modifier = modifier.fillMaxSize(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ImageWithDescription(
                painter = painterResource(Res.drawable.empty),
                description = stringResource(Res.string.no_tasks_text),
                imageSize = 200.dp,
            )
        }
    }
}
