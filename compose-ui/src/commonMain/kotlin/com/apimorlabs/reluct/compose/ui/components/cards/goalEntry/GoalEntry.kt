package com.apimorlabs.reluct.compose.ui.components.cards.goalEntry

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.LayoutDirection
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.cards.cardWithActions.ReluctDescriptionCard
import com.apimorlabs.reluct.compose.ui.components.textFields.text.EntryDescription
import com.apimorlabs.reluct.compose.ui.components.textFields.text.EntryHeading
import com.apimorlabs.reluct.compose.ui.no_description_text
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun GoalEntry(
    goal: Goal,
    onEntryClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
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
        ReluctDescriptionCard(
            modifier = modifier
                .drawBehind {
                    val strokeWidth = size.height
                    drawLinearIndicator(0f, progress.value, progressColor, strokeWidth)
                },
            containerColor = Color.Transparent,
            contentColor = cardContentColor.value,
            title = {
                EntryHeading(text = goal.name, color = LocalContentColor.current)
            },
            description = {
                EntryDescription(
                    text = goal.description.ifEmpty { stringResource(Res.string.no_description_text) },
                    color = LocalContentColor.current
                )
                GoalIntervalLabel(goal = goal)
                GoalTypeLabel(goalType = goal.goalType)
            },
            onClick = onEntryClick,
            rightItems = {
                Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = "Open")
            }
        )
    }
}

internal fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float
) {
    val width = size.width
    val height = size.height
    // Start drawing from the vertical center of the stroke
    val yOffset = height / 2

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    // Progress line
    drawLine(color, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth)
}

/*
@Preview
@Composable
private fun GoalEntryPreview() {
    ReluctAppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)) {
            PreviewData.goals.forEach { goal ->
                GoalEntry(goal = goal, onEntryClick = {})
            }
        }
    }
}*/
