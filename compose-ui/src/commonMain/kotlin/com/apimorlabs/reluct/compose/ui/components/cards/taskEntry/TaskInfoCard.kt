package com.apimorlabs.reluct.compose.ui.components.cards.taskEntry

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlarmOn
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.in_time_text
import com.apimorlabs.reluct.compose.ui.overdue_text
import com.apimorlabs.reluct.compose.ui.task_completed_date_time_text
import com.apimorlabs.reluct.compose.ui.task_info_due_text
import com.apimorlabs.reluct.compose.ui.task_info_reminder_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskInfoCard(
    task: Task,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .clip(shape)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.MediumPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(Dimens.SmallPadding.size)
        ) {
            TaskInfoEntry(
                text = stringResource(
                    Res.string.task_info_due_text,
                    task.dueDate,
                    task.dueTime
                ),
                icon = Icons.Rounded.Schedule,
                contentDescription = null
            )

            TaskInfoEntry(
                text = stringResource(
                    Res.string.task_info_reminder_text,
                    task.reminderFormatted
                ),
                icon = Icons.Rounded.NotificationsActive,
                contentDescription = null
            )

            TaskInfoEntry(
                text = if (task.overdue) {
                    stringResource(Res.string.overdue_text)
                } else {
                    stringResource(Res.string.in_time_text)
                },
                icon = Icons.Rounded.Timer,
                contentDescription = null,
                color = if (task.overdue) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color.Green
                }
            )

            if (task.done) {
                TaskInfoEntry(
                    text = stringResource(
                        Res.string.task_completed_date_time_text,
                        task.completedDateAndTime
                    ),
                    icon = Icons.Rounded.AlarmOn,
                    contentDescription = null
                )
            }

            if (!task.done) {
                TaskInfoEntry(
                    text = task.timeLeftLabel,
                    icon = Icons.Rounded.Timelapse,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
internal fun TaskInfoEntry(
    text: String,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = LocalContentColor.current,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(Dimens.SmallPadding.size),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
        TaskInfoText(
            modifier = modifier.fillMaxWidth(),
            text = text,
            style = textStyle,
            color = color
        )
    }
}

@Composable
internal fun TaskInfoText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = color
    )
}
