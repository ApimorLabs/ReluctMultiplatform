package com.apimorlabs.reluct.compose.ui.components.bottomSheet.addEditTask

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.tasks.EditTask
import com.apimorlabs.reluct.common.models.util.time.TimeUtils.getLocalDateTimeWithCorrectTimeZone
import com.apimorlabs.reluct.common.models.util.time.TimeUtils.plus
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.bottomSheet.taskLabels.TaskLabelsSelectCard
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.cards.settings.EntryWithCheckbox
import com.apimorlabs.reluct.compose.ui.components.textFields.ReluctTextField
import com.apimorlabs.reluct.compose.ui.description_hint
import com.apimorlabs.reluct.compose.ui.reminder_at
import com.apimorlabs.reluct.compose.ui.reminder_time_error_text
import com.apimorlabs.reluct.compose.ui.set_reminder
import com.apimorlabs.reluct.compose.ui.set_reminder_desc
import com.apimorlabs.reluct.compose.ui.task_labels_text
import com.apimorlabs.reluct.compose.ui.task_title_error_text
import com.apimorlabs.reluct.compose.ui.task_title_hint
import com.apimorlabs.reluct.compose.ui.task_to_be_done_at_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

// This provided here so that it doesn't leak DateTime dependencies to the
// screens modules.
@Composable
fun LazyColumnAddEditTaskFields(
    task: EditTask,
    saveButtonText: String,
    discardButtonText: String,
    onUpdateTask: (EditTask) -> Unit,
    onSave: (EditTask) -> Unit,
    onDiscard: () -> Unit,
    onEditLabels: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    onReminderSet: (reminderLocalDateTime: String) -> Unit = { },
) {
    val focusRequest = LocalFocusManager.current

    val setReminder by remember(task) {
        val reminderPresent = !task.reminderLocalDateTime.isNullOrBlank()
        mutableStateOf(reminderPresent)
    }

    var taskTitleError by remember { mutableStateOf(false) }

    var reminderTimePillError by remember { mutableStateOf(false) }

    val taskDueTime by remember(task.dueDateLocalDateTime) {
        derivedStateOf {
            if (task.dueDateLocalDateTime.isNotBlank()) {
                getLocalDateTimeWithCorrectTimeZone(
                    dateTime = task.dueDateLocalDateTime,
                    originalTimeZoneId = task.timeZoneId
                )
            } else {
                Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .plus(hours = 1)
            }
        }
    }

    val reminderTime by remember(task.reminderLocalDateTime) {
        derivedStateOf {
            task.reminderLocalDateTime?.let { timeString ->
                if (timeString.isNotBlank()) {
                    getLocalDateTimeWithCorrectTimeZone(
                        dateTime = timeString,
                        originalTimeZoneId = task.timeZoneId
                    )
                } else {
                    taskDueTime
                }
            } ?: taskDueTime
        }
    }

    LazyColumn(
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(Dimens.MediumPadding.size),
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
    ) {
        item {
            ReluctTextField(
                value = task.title,
                hint = stringResource(Res.string.task_title_hint),
                isError = taskTitleError,
                errorText = stringResource(Res.string.task_title_error_text),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequest.moveFocus(FocusDirection.Next) }
                ),
                onTextChange = { text ->
                    taskTitleError = false
                    onUpdateTask(task.copy(title = text))
                }
            )
        }

        item {
            ReluctTextField(
                modifier = Modifier
                    .height(200.dp),
                value = task.description ?: "",
                hint = stringResource(Res.string.description_hint),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                onTextChange = { text ->
                    onUpdateTask(task.copy(description = text))
                }
            )
        }

        // Task Labels
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(Res.string.task_labels_text),
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            TaskLabelsSelectCard(labels = task.taskLabels, onEditLabels = onEditLabels)
        }

        // Task Due Time
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(Res.string.task_to_be_done_at_text),
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            DateTimePills(
                currentLocalDateTime = taskDueTime,
                onLocalDateTimeChange = { dateTime ->
                    onUpdateTask(task.copy(dueDateLocalDateTime = dateTime.toString()))
                }
            )
        }

        // Task Reminder
        item {
            EntryWithCheckbox(
                isChecked = setReminder,
                title = stringResource(Res.string.set_reminder),
                description = stringResource(Res.string.set_reminder_desc),
                onCheckedChanged = { checked ->
                    val newTask = task.copy(
                        reminderLocalDateTime =
                        if (checked) {
                            taskDueTime.toString()
                        } else {
                            null
                        }
                    )
                    onUpdateTask(newTask)
                }
            )
        }

        item {
            AnimatedVisibility(
                visible = setReminder,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(Res.string.reminder_at),
                        style = MaterialTheme.typography.titleMedium,
                        color = LocalContentColor.current,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                    DateTimePills(
                        hasError = reminderTimePillError,
                        errorText = stringResource(Res.string.reminder_time_error_text),
                        currentLocalDateTime = reminderTime,
                        onLocalDateTimeChange = { dateTime ->
                            reminderTimePillError = dateTime <= taskDueTime
                            val newTime = if (dateTime > taskDueTime) {
                                dateTime
                            } else {
                                taskDueTime
                            }
                            onUpdateTask(task.copy(reminderLocalDateTime = newTime.toString()))
                        }
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReluctButton(
                    buttonText = discardButtonText,
                    icon = Icons.Rounded.DeleteSweep,
                    onButtonClicked = onDiscard,
                    shape = Shapes.large,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    buttonColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
                ReluctButton(
                    buttonText = saveButtonText,
                    icon = Icons.Rounded.Save,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        val isTitleBlank = task.title.isBlank()
                        taskTitleError = isTitleBlank
                        if (!isTitleBlank) onSave(task)

                        // Trigger Setting Reminder - Not used for now as it's set in Use Cases
                        task.reminderLocalDateTime?.let { onReminderSet(it) }
                    }
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    }
}
