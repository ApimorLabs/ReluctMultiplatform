package com.apimorlabs.reluct.compose.ui.components.cards.taskEntry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.cards.taskLabelEntry.TaskLabelPill
import com.apimorlabs.reluct.compose.ui.components.checkboxes.RoundCheckbox
import com.apimorlabs.reluct.compose.ui.components.textFields.text.EntryDescription
import com.apimorlabs.reluct.compose.ui.components.textFields.text.EntryHeading
import com.apimorlabs.reluct.compose.ui.no_description_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskEntry(
    task: Task,
    entryType: EntryType,
    onEntryClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    playAnimation: Boolean = false,
) {
    // Scale animation
    val animatedProgress = remember(task) {
        Animatable(initialValue = 0.8f)
    }
    if (playAnimation) {
        LaunchedEffect(key1 = task) {
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
        }
    }

    val animatedModifier = if (playAnimation) {
        modifier
            .graphicsLayer(
                scaleX = animatedProgress.value,
                scaleY = animatedProgress.value
            )
    } else {
        modifier
    }

    val showErrorColor = remember(task.overdue, entryType) {
        derivedStateOf {
            entryType == EntryType.TasksWithOverdue && task.overdue
        }
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (showErrorColor.value) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
        onClick = { onEntryClick() },
        modifier = animatedModifier
            .fillMaxWidth()
            .clip(Shapes.large)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            RoundCheckbox(
                isChecked = task.done,
                onCheckedChange = {
                    onCheckedChange(it)
                }
            )
            Spacer(modifier = Modifier.width(Dimens.SmallPadding.size))
            TaskEntryData(
                task = task,
                entryType = entryType
            )
        }
    }
}

@Composable
private fun TaskEntryData(
    task: Task,
    entryType: EntryType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            EntryHeading(text = task.title, modifier = Modifier.weight(1f), maxLines = 2)
            Spacer(modifier = Modifier.width(Dimens.SmallPadding.size))
            Text(
                text = task.dueTime,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current.copy(alpha = .8f)
            )
        }
        AnimatedVisibility(visible = entryType == EntryType.PendingTask) {
            EntryDescription(
                text = task.description.ifBlank { stringResource(Res.string.no_description_text) }
            )
        }
        TaskTimeInfo(
            timeText = if (
                entryType == EntryType.PendingTask ||
                entryType == EntryType.TasksWithOverdue
            ) {
                task.timeLeftLabel
            } else {
                task.dueDate
            },
            showOverdueLabel = entryType == EntryType.CompletedTask,
            overdue = task.overdue
        )

        // Task Labels
        if (task.taskLabels.isNotEmpty()) {
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(task.taskLabels, key = { it.id }) { item ->
                    TaskLabelPill(name = item.name, colorHex = item.colorHexString)
                }
            }
        }
    }
}

/*
@Preview
@Composable
internal fun TaskEntryPrev() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
        ) {
            TaskEntry(
                task = PreviewData.task1,
                entryType = EntryType.PendingTask,
                onEntryClick = {},
                onCheckedChange = {}
            )
            TaskEntry(
                task = PreviewData.task2,
                entryType = EntryType.CompletedTask,
                onEntryClick = {},
                onCheckedChange = {}
            )
            TaskEntry(
                task = PreviewData.task5,
                entryType = EntryType.CompletedTask,
                onEntryClick = {},
                onCheckedChange = {}
            )
            TaskEntry(
                task = PreviewData.task3,
                entryType = EntryType.Statistics,
                onEntryClick = {},
                onCheckedChange = {}
            )
            TaskEntry(
                task = PreviewData.task4,
                entryType = EntryType.TasksWithOverdue,
                onEntryClick = {},
                onCheckedChange = {}
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
        }
    }
}*/
