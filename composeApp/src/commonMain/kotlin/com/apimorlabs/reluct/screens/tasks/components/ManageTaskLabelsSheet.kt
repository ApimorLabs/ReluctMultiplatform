package com.apimorlabs.reluct.screens.tasks.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import com.apimorlabs.reluct.compose.ui.components.bottomSheet.taskLabels.AddEditTaskLabelSheet
import com.apimorlabs.reluct.compose.ui.components.bottomSheet.taskLabels.LazyColumnSelectTaskLabelsSheet
import com.apimorlabs.reluct.compose.ui.components.cards.taskLabelEntry.TaskLabelsEntryMode
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Stable
internal data class CurrentTaskLabels(
    val availableLabels: ImmutableList<TaskLabel>,
    val selectedLabels: ImmutableList<TaskLabel>,
    val onUpdateSelectedLabels: (ImmutableList<TaskLabel>) -> Unit
)

@Stable
internal sealed class TaskLabelsPage {
    class ModifyLabel(val label: TaskLabel? = null) : TaskLabelsPage()
    object ShowLabels : TaskLabelsPage()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun ManageTaskLabelsSheet(
    labelsState: CurrentTaskLabels,
    onSaveLabel: (TaskLabel) -> Unit,
    onDeleteLabel: (TaskLabel) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    startPage: TaskLabelsPage = TaskLabelsPage.ShowLabels,
    tonalElevation: Dp = 6.dp,
    shape: Shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    entryMode: TaskLabelsEntryMode = TaskLabelsEntryMode.SelectLabels,
) {
    val page = remember(startPage) {
        mutableStateOf(startPage)
    }
    Surface(
        modifier = modifier,
        tonalElevation = tonalElevation,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = shape
    ) {
        AnimatedContent(
            targetState = page.value,
            modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
            contentAlignment = Alignment.Center
        ) { targetState ->
            when (targetState) {
                is TaskLabelsPage.ShowLabels -> {
                    LazyColumnSelectTaskLabelsSheet(
                        entryMode = entryMode,
                        onClose = onClose,
                        availableLabels = labelsState.availableLabels,
                        selectedLabels = labelsState.selectedLabels,
                        onModifyLabel = { page.value = TaskLabelsPage.ModifyLabel(it) },
                        onEditLabels = { isAdd: Boolean, label: TaskLabel ->
                            labelsState.selectedLabels.toPersistentList().builder().apply {
                                if (isAdd) {
                                    add(label)
                                } else {
                                    remove(label)
                                }
                            }.build().also { newList ->
                                labelsState.onUpdateSelectedLabels(newList)
                            }
                        }
                    )
                }

                is TaskLabelsPage.ModifyLabel -> {
                    AddEditTaskLabelSheet(
                        onClose = { page.value = TaskLabelsPage.ShowLabels },
                        initialLabel = targetState.label,
                        onSaveLabel = onSaveLabel,
                        onDeleteLabel = onDeleteLabel
                    )
                }
            }
        }
    }
}
