package com.apimorlabs.reluct.compose.ui.components.cards.taskLabelEntry

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.cards.cardWithActions.ReluctDescriptionCard
import com.apimorlabs.reluct.compose.ui.components.checkboxes.RoundCheckbox
import com.apimorlabs.reluct.compose.ui.components.textFields.text.EntryDescription
import com.apimorlabs.reluct.compose.ui.components.textFields.text.EntryHeading
import com.apimorlabs.reluct.compose.ui.no_description_text
import com.apimorlabs.reluct.compose.ui.util.getContentColor
import com.apimorlabs.reluct.compose.ui.util.toColor
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskLabelEntry(
    label: TaskLabel,
    onEntryClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    entryMode: TaskLabelsEntryMode = TaskLabelsEntryMode.SelectLabels,
    isSelected: Boolean = false,
    onEdit: () -> Unit = {}
) {
    val labelColors by remember(label.colorHexString) {
        derivedStateOf {
            val color = label.colorHexString.toColor()
            val contentColor = color.getContentColor()
            color to contentColor
        }
    }

    val containerColor by animateColorAsState(
        targetValue = if (isSelected && entryMode == TaskLabelsEntryMode.SelectLabels) {
            labelColors.first
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected && entryMode == TaskLabelsEntryMode.SelectLabels) {
            labelColors.second
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )

    ReluctDescriptionCard(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        title = {
            EntryHeading(text = label.name, color = LocalContentColor.current)
        },
        description = {
            EntryDescription(
                text = label.description.ifBlank { stringResource(Res.string.no_description_text) },
                color = LocalContentColor.current
            )
        },
        onClick = onEntryClick,
        leftItems = {
            if (entryMode == TaskLabelsEntryMode.SelectLabels) {
                RoundCheckbox(
                    isChecked = isSelected,
                    onCheckedChange = {
                        onCheckedChange(it)
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = labelColors.first, shape = CircleShape)
                )
            }
        },
        rightItems = {
            if (entryMode == TaskLabelsEntryMode.SelectLabels) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = labelColors.first, shape = CircleShape)
                )
            } else {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                }
            }
        }
    )
}
