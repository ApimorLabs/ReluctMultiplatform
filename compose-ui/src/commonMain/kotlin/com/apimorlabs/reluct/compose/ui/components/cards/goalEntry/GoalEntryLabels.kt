package com.apimorlabs.reluct.compose.ui.components.cards.goalEntry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.common.models.domain.goals.GoalInterval
import com.apimorlabs.reluct.common.models.domain.goals.GoalType
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_screen_time_goal_type
import com.apimorlabs.reluct.compose.ui.components.cards.cardWithActions.ReluctDescriptionCard
import com.apimorlabs.reluct.compose.ui.components.textFields.text.EntryDescription
import com.apimorlabs.reluct.compose.ui.current_value_value_text
import com.apimorlabs.reluct.compose.ui.custom_interval_text
import com.apimorlabs.reluct.compose.ui.daily_interval_text
import com.apimorlabs.reluct.compose.ui.device_screen_time_goal_type
import com.apimorlabs.reluct.compose.ui.edit_button_text
import com.apimorlabs.reluct.compose.ui.interval_text
import com.apimorlabs.reluct.compose.ui.numeral_goal_type
import com.apimorlabs.reluct.compose.ui.sync_text
import com.apimorlabs.reluct.compose.ui.target_value_value_text
import com.apimorlabs.reluct.compose.ui.tasks_goal_type
import com.apimorlabs.reluct.compose.ui.tasks_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.weekly_interval_text
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun GoalIntervalLabel(
    goal: Goal,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    includeExtraText: Boolean = true
) {
    val dailyIntervalText = stringResource(Res.string.daily_interval_text)
    val weeklyIntervalText = stringResource(Res.string.weekly_interval_text)
    val customIntervalText = stringResource(Res.string.custom_interval_text)
    val intervalPreText = stringResource(Res.string.interval_text)

    val intervalText by remember(
        goal.goalDuration.goalInterval,
        goal.goalDuration.formattedTimeRange
    ) {
        derivedStateOf {
            when (goal.goalDuration.goalInterval) {
                GoalInterval.Daily -> dailyIntervalText
                GoalInterval.Weekly -> weeklyIntervalText
                GoalInterval.Custom -> {
                    val formatted = goal.goalDuration.formattedTimeRange
                    if (formatted != null) {
                        "${formatted.start} - ${formatted.endInclusive}"
                    } else {
                        customIntervalText
                    }
                }
            }
        }
    }

    val fullText by remember(intervalText) {
        derivedStateOf {
            val preText = if (includeExtraText) {
                "$intervalPreText : "
            } else {
                ""
            }
            preText + intervalText
        }
    }
    Text(
        modifier = modifier,
        text = fullText,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = color.copy(alpha = .8f)
    )
}

@Composable
internal fun GoalTypeLabel(
    goalType: GoalType,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    val tasksGoalTypeText = stringResource(Res.string.tasks_goal_type)
    val appGoalTypeText = stringResource(Res.string.app_screen_time_goal_type)
    val deviceGoalTypeText = stringResource(Res.string.device_screen_time_goal_type)
    val numeralGoalTypeText = stringResource(Res.string.numeral_goal_type)

    val goalTypeText by remember(goalType) {
        derivedStateOf {
            when (goalType) {
                GoalType.TasksGoal -> tasksGoalTypeText
                GoalType.AppScreenTimeGoal -> appGoalTypeText
                GoalType.DeviceScreenTimeGoal -> deviceGoalTypeText
                GoalType.NumeralGoal -> numeralGoalTypeText
            }
        }
    }

    Box(
        modifier = Modifier
            .clip(Shapes.large)
            .background(containerColor) then modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = Dimens.SmallPadding.size,
                horizontal = Dimens.MediumPadding.size
            ),
            text = goalTypeText,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun GoalTypeAndIntervalLabels(
    goal: Goal,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GoalTypeLabel(
            goalType = goal.goalType,
            containerColor = containerColor,
            contentColor = contentColor
        )
        Box(
            modifier = Modifier
                .clip(Shapes.large)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            GoalIntervalLabel(
                modifier = Modifier.padding(
                    vertical = Dimens.SmallPadding.size,
                    horizontal = Dimens.MediumPadding.size
                ),
                goal = goal,
                color = contentColor,
                includeExtraText = false
            )
        }
    }
}

@Composable
fun GoalValuesCard(
    goal: Goal,
    onUpdateClicked: (GoalType) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val tasksPlurals = pluralStringResource(
        Res.plurals.tasks_text,
        goal.targetValue.toInt(),
        goal.targetValue
    )

    val targetValueText by remember(goal.targetValue) {
        derivedStateOf {
            when (goal.goalType) {
                GoalType.AppScreenTimeGoal ->
                    TimeUtils.getFormattedTimeDurationString(goal.targetValue)

                GoalType.DeviceScreenTimeGoal ->
                    TimeUtils.getFormattedTimeDurationString(goal.targetValue)

                GoalType.TasksGoal -> tasksPlurals
                else -> goal.targetValue
            }
        }
    }

    val currentTasksPlurals = pluralStringResource(
        Res.plurals.tasks_text,
        goal.currentValue.toInt(),
        goal.currentValue
    )
    val currentValueText by remember(goal.currentValue) {
        derivedStateOf {
            when (goal.goalType) {
                GoalType.AppScreenTimeGoal ->
                    TimeUtils.getFormattedTimeDurationString(goal.currentValue)

                GoalType.DeviceScreenTimeGoal ->
                    TimeUtils.getFormattedTimeDurationString(goal.currentValue)

                GoalType.TasksGoal -> currentTasksPlurals
                else -> goal.currentValue
            }
        }
    }

    ReluctDescriptionCard(
        modifier = modifier,
        contentColor = contentColor,
        containerColor = containerColor,
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)) {
                EntryDescription(
                    text = stringResource(
                        Res.string.current_value_value_text,
                        currentValueText
                    )
                )
                EntryDescription(
                    text = stringResource(
                        Res.string.target_value_value_text,
                        targetValueText
                    )
                )
            }
        },
        description = {},
        onClick = { onUpdateClicked(goal.goalType) },
        rightItems = {
            IconButton(onClick = { onUpdateClicked(goal.goalType) }, enabled = !isLoading) {
                if (goal.goalType == GoalType.NumeralGoal) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = stringResource(Res.string.edit_button_text)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = stringResource(Res.string.sync_text)
                    )
                }
            }
        }
    )
}
