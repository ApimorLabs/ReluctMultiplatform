package com.apimorlabs.reluct.compose.ui.components.bottomSheet.taskLabels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.add_files
import com.apimorlabs.reluct.compose.ui.components.bottomSheet.TopSheetSection
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.cards.taskLabelEntry.TaskLabelEntry
import com.apimorlabs.reluct.compose.ui.components.cards.taskLabelEntry.TaskLabelsEntryMode
import com.apimorlabs.reluct.compose.ui.components.images.ImageWithDescription
import com.apimorlabs.reluct.compose.ui.new_task_label_text
import com.apimorlabs.reluct.compose.ui.no_saved_label_text
import com.apimorlabs.reluct.compose.ui.select_task_labels_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnSelectTaskLabelsSheet(
    onClose: () -> Unit,
    availableLabels: ImmutableList<TaskLabel>,
    selectedLabels: ImmutableList<TaskLabel>,
    onModifyLabel: (TaskLabel?) -> Unit,
    onEditLabels: (isAdd: Boolean, label: TaskLabel) -> Unit,
    modifier: Modifier = Modifier,
    entryMode: TaskLabelsEntryMode = TaskLabelsEntryMode.SelectLabels,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
    ) {
        stickyHeader {
            TopSheetSection(
                sheetTitle = stringResource(Res.string.select_task_labels_text),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onCloseClicked = onClose
            )
        }

        if (availableLabels.isEmpty()) {
            item {
                ImageWithDescription(
                    painter = painterResource(Res.drawable.add_files),
                    imageSize = 200.dp,
                    description = stringResource(Res.string.no_saved_label_text),
                )
            }
        } else {
            items(availableLabels, key = { it.id }) { item ->
                val selected by remember(selectedLabels) {
                    derivedStateOf {
                        selectedLabels.any { it.id == item.id }
                    }
                }

                TaskLabelEntry(
                    entryMode = entryMode,
                    label = item,
                    isSelected = selected,
                    onEntryClick = { onEditLabels(!selected, item) },
                    onCheckedChange = { onEditLabels(it, item) },
                    onEdit = { onModifyLabel(item) }
                )
            }
        }

        item {
            ReluctButton(
                modifier = Modifier.fillMaxWidth(.7f),
                shape = Shapes.large,
                buttonText = stringResource(Res.string.new_task_label_text),
                icon = Icons.Rounded.Add,
                onButtonClicked = { onModifyLabel(null) }
            )
        }

        // Bottom Space
        item {
            Spacer(
                modifier = Modifier
                    .padding(bottom = Dimens.MediumPadding.size)
                    .navigationBarsPadding()
            )
        }
    }
}
