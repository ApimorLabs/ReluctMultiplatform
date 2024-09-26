package com.apimorlabs.reluct.compose.ui.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.components.animations.slideInVerticallyFadeReversed
import com.apimorlabs.reluct.compose.ui.components.animations.slideOutVerticallyFadeReversed
import com.apimorlabs.reluct.compose.ui.util.ScrollContext

@Composable
fun CollapsingFloatingButton(
    scrollContextState: State<ScrollContext>,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues = PaddingValues(0.dp),
) {
    AnimatedVisibility(
        visible = scrollContextState.value.isTop,
        enter = slideInVerticallyFadeReversed(),
        exit = slideOutVerticallyFadeReversed()
    ) {
        ReluctFloatingActionButton(
            modifier = modifier.padding(bottom = mainScaffoldPadding.calculateBottomPadding()),
            buttonText = text,
            contentDescription = text,
            icon = icon,
            onButtonClicked = onClick
        )
    }
}
