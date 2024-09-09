package com.apimorlabs.reluct.compose.ui.components.topBar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.back_icon
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

/**
 * This is the base top bar for the app
 */
@Composable
fun ReluctTopBarBase(
    toolbarHeading: String?,
    content: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
    onBackButtonPressed: () -> Unit = { },
    contentAlignment: Alignment = Alignment.Center,
    shape: Shape = Shapes.large,
    collapsedBackgroundColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = LocalContentColor.current,
    containerColor: Color = MaterialTheme.colorScheme.background,
    collapsed: Boolean = false,
) {
    val animatedElevation by animateDpAsState(
        targetValue = if (collapsed) 10.dp else 0.dp,
        animationSpec = tween(500, easing = LinearOutSlowInEasing)
    )
    val animatedTitleAlpha by animateFloatAsState(
        targetValue = if (!toolbarHeading.isNullOrBlank()) {
            if (collapsed) 1f else 0f
        } else {
            0f
        },
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    )
    val animatedColor by animateColorAsState(
        targetValue = if (collapsed) {
            collapsedBackgroundColor
        } else {
            containerColor
        },
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .animateContentSize()
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
                            contentDescription = stringResource(resource = Res.string.back_icon),
                            tint = contentColor
                        )
                    }
                }
                toolbarHeading?.let {
                    Text(
                        text = toolbarHeading,
                        color = contentColor.copy(
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
                    .alpha(1 - animatedTitleAlpha),
                contentAlignment = contentAlignment,
                content = content
            )
        }
    }
}
