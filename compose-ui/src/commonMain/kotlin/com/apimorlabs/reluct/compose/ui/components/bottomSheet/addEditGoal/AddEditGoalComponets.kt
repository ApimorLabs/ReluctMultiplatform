package com.apimorlabs.reluct.compose.ui.components.bottomSheet.addEditGoal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import com.apimorlabs.reluct.common.models.domain.goals.GoalInterval
import com.apimorlabs.reluct.common.models.domain.goals.GoalType
import com.apimorlabs.reluct.common.models.util.time.TimeUtils.plus
import com.apimorlabs.reluct.common.models.util.time.Week
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_screen_time_goal_type
import com.apimorlabs.reluct.compose.ui.app_time_limit
import com.apimorlabs.reluct.compose.ui.choose_days_txt
import com.apimorlabs.reluct.compose.ui.components.bottomSheet.addEditTask.DateTimePills
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.cards.date.SelectedDaysOfWeekViewer
import com.apimorlabs.reluct.compose.ui.components.numberPicker.HoursNumberPicker
import com.apimorlabs.reluct.compose.ui.components.numberPicker.NumberPicker
import com.apimorlabs.reluct.compose.ui.components.numberPicker.convertMillisToTime
import com.apimorlabs.reluct.compose.ui.components.numberPicker.convertTimeToMillis
import com.apimorlabs.reluct.compose.ui.components.textFields.ReluctTextField
import com.apimorlabs.reluct.compose.ui.custom_interval_text
import com.apimorlabs.reluct.compose.ui.daily_interval_text
import com.apimorlabs.reluct.compose.ui.device_screen_time_goal_type
import com.apimorlabs.reluct.compose.ui.end_time_txt
import com.apimorlabs.reluct.compose.ui.enter_number_of_tasks_txt
import com.apimorlabs.reluct.compose.ui.enter_target_value
import com.apimorlabs.reluct.compose.ui.goal_time_error_text
import com.apimorlabs.reluct.compose.ui.invalid_value_text
import com.apimorlabs.reluct.compose.ui.no_apps_text
import com.apimorlabs.reluct.compose.ui.numeral_goal_type
import com.apimorlabs.reluct.compose.ui.select_number_of_tasks_txt
import com.apimorlabs.reluct.compose.ui.start_time_txt
import com.apimorlabs.reluct.compose.ui.target_value_txt
import com.apimorlabs.reluct.compose.ui.tasks_goal_type
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.weekly_interval_text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun GoalTypeSelector(
    selectedGoalType: GoalType,
    onSelectGoalType: (GoalType) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Composable
    fun getGoalTypeString(goalType: GoalType): String =
        when (goalType) {
            GoalType.DeviceScreenTimeGoal -> stringResource(Res.string.device_screen_time_goal_type)
            GoalType.AppScreenTimeGoal -> stringResource(Res.string.app_screen_time_goal_type)
            GoalType.TasksGoal -> stringResource(Res.string.tasks_goal_type)
            GoalType.NumeralGoal -> stringResource(Res.string.numeral_goal_type)
        }

    val rowState = rememberLazyListState()
    var selectedItemIndex by remember { mutableStateOf(0) }

    LaunchedEffect(selectedItemIndex) {
        if (selectedItemIndex != 0) {
            rowState.animateScrollToItem(selectedItemIndex)
        }
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = rowState,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        itemsIndexed(GoalType.entries) { index, type ->
            selectedItemIndex = if (selectedGoalType == type) index else selectedItemIndex

            ReluctButton(
                buttonText = getGoalTypeString(goalType = type),
                icon = null,
                shape = Shapes.large,
                buttonColor = if (selectedGoalType == type) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                contentColor = if (selectedGoalType == type) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                onButtonClicked = { onSelectGoalType(type) }
            )
        }
    }
}

@Composable
internal fun GoalIntervalSelector(
    selectedGoalInterval: GoalInterval,
    onSelectGoalInterval: (GoalInterval) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Composable
    fun getGoalTypeString(goalInterval: GoalInterval): String =
        when (goalInterval) {
            GoalInterval.Daily -> stringResource(Res.string.daily_interval_text)
            GoalInterval.Weekly -> stringResource(Res.string.weekly_interval_text)
            GoalInterval.Custom -> stringResource(Res.string.custom_interval_text)
        }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        GoalInterval.entries.forEach { item ->
            ReluctButton(
                modifier = Modifier.weight(1f),
                buttonText = getGoalTypeString(goalInterval = item),
                icon = null,
                shape = Shapes.large,
                buttonColor = if (selectedGoalInterval == item) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                contentColor = if (selectedGoalInterval == item) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                onButtonClicked = { onSelectGoalInterval(item) }
            )
        }
    }
}

@Composable
internal fun AddEditGoalItemTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    maxLines: Int = 1,
) {
    Text(
        modifier = modifier
            .fillMaxWidth(),
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun SelectedAppsCard(
    apps: ImmutableList<AppInfo>,
    onEditApps: () -> Unit,
    modifier: Modifier = Modifier,
    appIconSize: Dp = 36.dp,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onEditApps
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier
                    .height(appIconSize)
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
            ) {
                if (apps.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(Res.string.no_apps_text),
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalContentColor.current,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    items(apps) { app ->
                        Image(
                            modifier = Modifier.size(appIconSize),
                            bitmap = app.appIcon.icon.decodeToImageBitmap(),
                            contentDescription = app.appName
                        )
                    }
                }
            }

            Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
        }
    }
}

@Composable
fun GoalScreenTimePicker(
    currentTimeMillis: Long,
    onUpdateCurrentTime: (timeMillis: Long) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val pickerValue by remember(currentTimeMillis) {
        derivedStateOf {
            convertMillisToTime(currentTimeMillis)
        }
    }

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        HoursNumberPicker(
            modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
            value = pickerValue,
            onValueChange = { onUpdateCurrentTime(convertTimeToMillis(it)) },
            dividersColor = contentColor,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                color = contentColor
            ),
            hoursDivider = {
                Text(
                    text = "hr",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(color = contentColor)
                )
            },
            minutesDivider = {
                Text(
                    text = "m",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(color = contentColor)
                )
            }
        )
    }
}

internal fun LazyListScope.goalTargetValuePicker(
    keyboardController: SoftwareKeyboardController?,
    goalType: GoalType,
    targetValue: Long,
    onUpdateTargetValue: (Long) -> Unit
) {
    if (goalType == GoalType.DeviceScreenTimeGoal || goalType == GoalType.AppScreenTimeGoal) {
        item {
            AddEditGoalItemTitle(
                modifier = Modifier.animateItem(),
                text = stringResource(Res.string.app_time_limit),
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            GoalScreenTimePicker(
                modifier = Modifier.animateItem(),
                currentTimeMillis = targetValue,
                onUpdateCurrentTime = onUpdateTargetValue
            )
        }
    } else {
        item {
            var textValue by remember { mutableStateOf(targetValue.toString()) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AddEditGoalItemTitle(
                    modifier = Modifier
                        .weight(1f)
                        .animateItem(),
                    text = if (goalType == GoalType.TasksGoal) {
                        stringResource(Res.string.select_number_of_tasks_txt)
                    } else {
                        stringResource(Res.string.target_value_txt)
                    },
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(Dimens.MediumPadding.size))

                ReluctTextField(
                    value = textValue,
                    isError = textValue.isBlank(),
                    errorText = stringResource(Res.string.invalid_value_text),
                    singleLine = true,
                    hint = if (goalType == GoalType.TasksGoal) {
                        stringResource(Res.string.enter_number_of_tasks_txt)
                    } else {
                        stringResource(Res.string.enter_target_value)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    onTextChange = { text ->
                        textValue = text
                        if (text.isNotBlank()) {
                            text.toLongOrNull()?.run(onUpdateTargetValue)
                                ?: run { textValue = "" }
                        }
                    },
                    textStyle = MaterialTheme.typography.titleMedium
                        .copy(textAlign = TextAlign.Center),
                    modifier = Modifier.animateItem().fillMaxWidth(.6f)
                )
            }
        }
    }
}

internal fun LazyListScope.goalDurationPicker(
    dateTimeRange: ClosedRange<LocalDateTime>,
    currentDaysOfWeek: ImmutableList<Week>,
    goalInterval: GoalInterval,
    onDateTimeRangeChange: (ClosedRange<LocalDateTime>) -> Unit,
    onUpdateDaysOfWeek: (ImmutableList<Week>) -> Unit
) {
    when (goalInterval) {
        GoalInterval.Daily -> {
            item {
                AddEditGoalItemTitle(
                    modifier = Modifier.animateItem(),
                    text = stringResource(Res.string.choose_days_txt),
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                SelectedDaysOfWeekViewer(
                    modifier = Modifier.animateItem(),
                    selectedDays = currentDaysOfWeek,
                    onUpdateDaysOfWeek = onUpdateDaysOfWeek
                )
            }
        }

        GoalInterval.Custom -> {
            item {
                AddEditGoalItemTitle(
                    modifier = Modifier.animateItem(),
                    text = stringResource(Res.string.start_time_txt),
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                DateTimePills(
                    modifier = Modifier.animateItem(),
                    currentLocalDateTime = dateTimeRange.start,
                    onLocalDateTimeChange = { dateTime ->
                        val endTime = if (dateTime >= dateTimeRange.endInclusive) {
                            dateTime.plus(hours = 1)
                        } else {
                            dateTimeRange.endInclusive
                        }
                        onDateTimeRangeChange(dateTime..endTime)
                    }
                )
            }

            item {
                var endTimeError by remember { mutableStateOf(false) }

                AddEditGoalItemTitle(
                    modifier = Modifier.animateItem(),
                    text = stringResource(Res.string.end_time_txt),
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                DateTimePills(
                    modifier = Modifier.animateItem(),
                    hasError = endTimeError,
                    errorText = stringResource(Res.string.goal_time_error_text),
                    currentLocalDateTime = dateTimeRange.endInclusive,
                    onLocalDateTimeChange = { dateTime ->
                        endTimeError = dateTime <= dateTimeRange.start
                        val endTime =
                            if (dateTime <= dateTimeRange.start) {
                                dateTimeRange.endInclusive
                            } else {
                                dateTime
                            }
                        onDateTimeRangeChange(dateTimeRange.start..endTime)
                    }
                )
            }
        }

        else -> {}
    }
}

@Composable
private fun GoalNumberPicker(
    name: String,
    currentValue: Long,
    onUpdateCurrentValue: (Long) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    numberRange: IntRange = 0..10000
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        AddEditGoalItemTitle(
            modifier = Modifier.padding(Dimens.MediumPadding.size),
            text = name
        )
        NumberPicker(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxWidth(),
            value = currentValue.toInt(),
            onValueChange = { onUpdateCurrentValue(it.toLong()) },
            range = numberRange,
            textStyle = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
internal fun ReluctSelectionButton(
    isSelected: Boolean,
    content: @Composable ColumnScope.() -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        ),
        content = content
    )
}
