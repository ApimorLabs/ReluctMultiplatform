package com.apimorlabs.reluct.compose.ui.components.topBar.toolbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.back_icon
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

/**
 * Collapsing Toolbar that can be used in a topBar slot of Scaffold.
 * It has a back button, default bottom rounded corners
 * & a box scope which holds content centered by default.
 * You need to implement nestedScrollConnection to set the offset values
 * See Usage of this in DashboardScreen or TasksScreen or GoalsScreen
 *
 * To use this Toolbar without a heading text just make toolbarHeading `null`
 * To Disable the back button at the top set showBackButton to false
 *
 * With nestedScrollConnection know that the maximum offset that can be
 * reached is -132.0
 */
@Composable
fun CollapsingToolbarBase(
    toolbarHeading: String?,
    toolbarHeight: Dp,
    toolbarOffset: Float,
    onCollapsed: (Boolean) -> Unit,
    content: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
    onBackButtonPressed: () -> Unit = { },
    contentAlignment: Alignment = Alignment.Center,
    shape: Shape = Shapes.large,
    collapsedBackgroundColor: Color = MaterialTheme.colorScheme.background,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    minShrinkHeight: Dp = 100.dp,
) {
    val scrollDp = toolbarHeight + toolbarOffset.dp
    val collapsed by remember(scrollDp) {
        mutableStateOf(
            scrollDp < minShrinkHeight + 20.dp
        )
    }
    val animatedCardSize by animateDpAsState(
        targetValue = if (scrollDp <= minShrinkHeight) minShrinkHeight else scrollDp,
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    )
    val animatedElevation by animateDpAsState(
        targetValue = if (scrollDp < minShrinkHeight + 20.dp) 10.dp else 0.dp,
        animationSpec = tween(500, easing = LinearOutSlowInEasing)
    )
    val animatedTitleAlpha by animateFloatAsState(
        targetValue = if (!toolbarHeading.isNullOrBlank()) {
            if (scrollDp <= minShrinkHeight + 20.dp) 1f else 0f
        } else {
            0f
        },
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    )
    val animatedColor by animateColorAsState(
        targetValue = if (scrollDp < minShrinkHeight + 20.dp) {
            collapsedBackgroundColor
        } else {
            backgroundColor
        },
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    )

    LaunchedEffect(key1 = collapsed) {
        onCollapsed(collapsed)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = animatedElevation,
                shape = shape
            )
            .background(
                color = animatedColor,
                shape = shape
            )
    ) {
        Box(
            modifier = modifier
                .height(animatedCardSize)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBackButton) {
                    IconButton(
                        onClick = onBackButtonPressed,
                        modifier = Modifier
                            .padding(Dimens.SmallPadding.size)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(Res.string.back_icon),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                toolbarHeading?.let {
                    Text(
                        text = toolbarHeading,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                            alpha = animatedTitleAlpha
                        ),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(horizontal = Dimens.SmallPadding.size)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(1 - animatedTitleAlpha),
                contentAlignment = contentAlignment,
                content = content
            )
        }
    }
}

/*
@Preview
@Composable
fun CollapsingToolbarPrev() {
    ReluctAppTheme {
        CollapsingToolbarBase(
            toolbarHeading = "Settings",
            toolbarOffset = 0f,
            toolbarHeight = 300.dp,
            onCollapsed = {},
            content = {
                Text(
                    text = "Settings",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(horizontal = Dimens.SmallPadding.size)
                        .animateContentSize(
                            animationSpec = tween(
                                300,
                                easing = LinearOutSlowInEasing
                            )
                        )
                )
            }
        )
    }
}*/
