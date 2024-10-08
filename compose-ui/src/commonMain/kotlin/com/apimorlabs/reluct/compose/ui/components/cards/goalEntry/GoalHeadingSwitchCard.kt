package com.apimorlabs.reluct.compose.ui.components.cards.goalEntry

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.cards.cardWithActions.ReluctSwitchCard
import com.apimorlabs.reluct.compose.ui.no_description_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun GoalHeadingSwitchCard(
    goal: Goal,
    onToggleActiveState: (goalId: String, isActive: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    shape: Shape = Shapes.large
) {
    val progressValue by remember(goal.currentValue) {
        derivedStateOf {
            if (goal.targetValue <= 0) {
                0f
            } else {
                1f * goal.currentValue / goal.targetValue
            }
        }
    }
    val progress = remember { Animatable(initialValue = 0f) }
    val progressColor by animateColorAsState(
        targetValue = if (progressValue > 1f) {
            MaterialTheme.colorScheme.error.copy(alpha = .7f)
        } else {
            MaterialTheme.colorScheme.primary.copy(alpha = .3f)
        }
    )
    val cardContentColor = remember { Animatable(contentColor) }
    val onErrorColor = MaterialTheme.colorScheme.onError

    LaunchedEffect(progressValue) {
        launch {
            progress.animateTo(
                targetValue = progressValue,
                animationSpec = TweenSpec(durationMillis = 1000)
            )
        }
        launch {
            cardContentColor.animateTo(
                targetValue = if (progressValue > 1f) {
                    onErrorColor
                } else {
                    contentColor
                },
                animationSpec = TweenSpec(durationMillis = 1000)
            )
        }
    }

    Box(
        modifier = Modifier
            .clip(shape)
            .background(containerColor)
            then modifier,
        contentAlignment = Alignment.Center
    ) {
        ReluctSwitchCard(
            modifier = Modifier
                .drawBehind {
                    val strokeWidth = size.height
                    drawLinearIndicator(0f, progress.value, progressColor, strokeWidth)
                },
            checked = goal.isActive,
            onCheckedChange = { onToggleActiveState(goal.id, it) },
            title = {
                Text(
                    modifier = Modifier,
                    text = goal.name,
                    style = textStyle,
                    color = LocalContentColor.current
                )
            },
            description = {},
            bottomContent = {
                HorizontalDivider(
                    thickness = Dimens.MediumPadding.size,
                    color = LocalContentColor.current
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = Dimens.MediumPadding.size)
                        .padding(
                            bottom = Dimens.MediumPadding.size,
                            top = Dimens.SmallPadding.size
                        ),
                    text = goal.description.ifBlank { stringResource(Res.string.no_description_text) },
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalContentColor.current
                )
            },
            onClick = { onToggleActiveState(goal.id, !goal.isActive) },
            containerColor = Color.Transparent,
            contentColor = cardContentColor.value,
            shape = shape
        )
    }
}
