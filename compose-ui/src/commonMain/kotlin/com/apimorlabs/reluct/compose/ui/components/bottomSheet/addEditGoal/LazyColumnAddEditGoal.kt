package com.apimorlabs.reluct.compose.ui.components.bottomSheet.addEditGoal

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.common.models.domain.goals.GoalInterval
import com.apimorlabs.reluct.common.models.domain.goals.GoalType
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.common.models.util.time.TimeUtils.plus
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_list_header
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.textFields.ReluctTextField
import com.apimorlabs.reluct.compose.ui.description_hint
import com.apimorlabs.reluct.compose.ui.discard_button_text
import com.apimorlabs.reluct.compose.ui.goal_name_error_text
import com.apimorlabs.reluct.compose.ui.goal_name_text
import com.apimorlabs.reluct.compose.ui.goal_type_text
import com.apimorlabs.reluct.compose.ui.interval_text
import com.apimorlabs.reluct.compose.ui.save_button_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@Composable
fun LazyColumnAddEditGoal(
    goal: Goal,
    onUpdateGoal: (Goal) -> Unit,
    onDiscard: () -> Unit,
    onSave: (goal: Goal) -> Unit,
    onShowAppPicker: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val focusRequest = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var goalNameError by remember { mutableStateOf(false) }

    val localDateTimeRange by remember(goal.goalDuration.timeRangeInMillis) {
        derivedStateOf {
            goal.goalDuration.timeRangeInMillis?.let { range ->
                val start = TimeUtils.epochMillisToLocalDateTime(range.first)
                val end = TimeUtils.epochMillisToLocalDateTime(range.last)
                start..end
            } ?: run {
                val start = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                val end = start.plus(days = 1)
                println(start..end)
                return@run start..end
            }
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
                value = goal.name,
                hint = stringResource(Res.string.goal_name_text),
                isError = goalNameError,
                errorText = stringResource(Res.string.goal_name_error_text),
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
                    onUpdateGoal(goal.copy(name = text))
                }
            )
        }

        item {
            ReluctTextField(
                modifier = Modifier
                    .height(200.dp),
                value = goal.description,
                hint = stringResource(Res.string.description_hint),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                onTextChange = { text ->
                    onUpdateGoal(goal.copy(description = text))
                }
            )
        }

        // Goal Type Selector
        item {
            AddEditGoalItemTitle(text = stringResource(Res.string.goal_type_text))
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            GoalTypeSelector(
                selectedGoalType = goal.goalType,
                onSelectGoalType = { type -> onUpdateGoal(goal.copy(goalType = type)) }
            )
        }

        // Show Apps Selector
        if (goal.goalType == GoalType.AppScreenTimeGoal) {
            item {
                AddEditGoalItemTitle(
                    modifier = Modifier.animateItem(),
                    text = stringResource(Res.string.app_list_header),
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                SelectedAppsCard(
                    modifier = Modifier.animateItem(),
                    apps = goal.relatedApps,
                    onEditApps = onShowAppPicker
                )
            }
        }

        // Target Value Manipulation
        goalTargetValuePicker(
            keyboardController = keyboardController,
            goalType = goal.goalType,
            targetValue = goal.targetValue,
            onUpdateTargetValue = { onUpdateGoal(goal.copy(targetValue = it)) }
        )

        // Goal Interval Selector
        item {
            AddEditGoalItemTitle(
                modifier = Modifier.animateItem(),
                text = stringResource(Res.string.interval_text)
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            GoalIntervalSelector(
                modifier = Modifier.animateItem(),
                selectedGoalInterval = goal.goalDuration.goalInterval,
                onSelectGoalInterval = { interval ->
                    val duration = goal.goalDuration.let {
                        if (interval == GoalInterval.Custom) {
                            it.copy(goalInterval = interval)
                        } else {
                            it.copy(goalInterval = interval, timeRangeInMillis = null)
                        }
                    }
                    onUpdateGoal(goal.copy(goalDuration = duration))
                }
            )
        }

        // Goal Interval Duration Selection
        goalDurationPicker(
            dateTimeRange = localDateTimeRange,
            currentDaysOfWeek = goal.goalDuration.selectedDaysOfWeek,
            goalInterval = goal.goalDuration.goalInterval,
            onDateTimeRangeChange = { dateTimeRange ->
                val start = dateTimeRange.start.toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
                val end = dateTimeRange.endInclusive.toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
                val duration = goal.goalDuration.copy(timeRangeInMillis = start..end)
                onUpdateGoal(goal.copy(goalDuration = duration))
            },
            onUpdateDaysOfWeek = { daysOfWeek ->
                val duration = goal.goalDuration.copy(selectedDaysOfWeek = daysOfWeek)
                onUpdateGoal(goal.copy(goalDuration = duration))
            }
        )

        // Buttons
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReluctButton(
                    buttonText = stringResource(Res.string.discard_button_text),
                    icon = Icons.Rounded.DeleteSweep,
                    onButtonClicked = onDiscard,
                    shape = Shapes.large,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    buttonColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
                ReluctButton(
                    buttonText = stringResource(Res.string.save_button_text),
                    icon = Icons.Rounded.Save,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        val isTitleBlank = goal.name.isBlank()
                        goalNameError = isTitleBlank
                        if (!isTitleBlank) onSave(goal)
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

/*
@Preview
@Composable
private fun AddEditGoalPreview() {
    ReluctAppTheme {
        Surface(color = MaterialTheme.colorScheme.background.copy(.7f)) {
            LazyColumnAddEditGoal(
                goal = PreviewData.goals.last(),
                onUpdateGoal = {},
                onDiscard = {},
                onSave = {},
                onShowAppPicker = {}
            )
        }
    }
}*/
