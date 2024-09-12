package com.apimorlabs.reluct.compose.ui.components.datetime

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.dialogs.DateTimeDialog
import com.apimorlabs.reluct.compose.ui.components.dialogs.DesktopWindowConfig
import com.apimorlabs.reluct.compose.ui.components.dialogs.MultiplatformDialogProperties
import com.apimorlabs.reluct.compose.ui.date_picker
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.time_picker
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DateTimePills(
    currentLocalDateTime: LocalDateTime,
    onLocalDateTimeChange: (dateTime: LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
    dialogShape: Shape = Shapes.large,
    hasError: Boolean = false,
    errorText: String = "",
) {

    val pillContainerColor by animateColorAsState(
        targetValue = if (hasError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    )
    val pillContentColor by animateColorAsState(
        targetValue = if (hasError) {
            MaterialTheme.colorScheme.onError
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )

    val dateString = rememberSaveable(currentLocalDateTime) {
        mutableStateOf(
            TimeUtils.getFormattedDateString(
                dateTime = currentLocalDateTime.toString(),
                originalTimeZoneId = TimeZone.currentSystemDefault().id
            )
        )
    }

    val timeString = rememberSaveable(currentLocalDateTime) {
        mutableStateOf(
            TimeUtils.getTimeFromLocalDateTime(
                dateTime = currentLocalDateTime.toString(),
                originalTimeZoneId = TimeZone.currentSystemDefault().id
            )
        )
    }

    val showDatePicker = rememberSaveable(currentLocalDateTime) { mutableStateOf(false) }
    val showTimePicker = rememberSaveable(currentLocalDateTime) { mutableStateOf(false) }

    // Date and Time Picker Dialogs initiation
    DateAndTimeMaterialDialogs(
        initialLocalDateTime = currentLocalDateTime,
        shape = dialogShape,
        showDatePicker = showDatePicker,
        showTimePicker = showTimePicker,
        onCloseDialogs = {
            showDatePicker.value = false
            showTimePicker.value = false
        },
        onLocalDateTimeChange = { dateTimeChange ->
            onLocalDateTimeChange(dateTimeChange)
        },
    )

    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
            modifier = modifier
                .fillMaxWidth()
        ) {
            // Date
            ReluctButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = dateString.value,
                icon = Icons.Rounded.DateRange,
                onButtonClicked = { showDatePicker.value = true },
                shape = Shapes.large,
                contentColor = pillContentColor,
                buttonColor = pillContainerColor
            )

            // Time
            ReluctButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = timeString.value,
                icon = Icons.Rounded.Schedule,
                onButtonClicked = { showTimePicker.value = true },
                shape = Shapes.large,
                contentColor = pillContentColor,
                buttonColor = pillContainerColor
            )
        }

        if (hasError && errorText.isNotBlank()) {
            Text(text = errorText, color = MaterialTheme.colorScheme.error)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateAndTimeMaterialDialogs(
    initialLocalDateTime: LocalDateTime,
    showTimePicker: State<Boolean>,
    showDatePicker: State<Boolean>,
    onCloseDialogs: () -> Unit,
    onLocalDateTimeChange: (dateTimeChange: LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large
) {
    var dateTime by remember { mutableStateOf(initialLocalDateTime) }
    val timeDesktopWindowConfig = DesktopWindowConfig(
        desktopWindowSize = DpSize(600.dp, 550.dp),
        title = stringResource(Res.string.time_picker)
    )
    val dateDesktopWindowConfig = DesktopWindowConfig(
        desktopWindowSize = DpSize(600.dp, 550.dp),
        title = stringResource(Res.string.date_picker)
    )

    val timePickerState = rememberTimePickerState(
        initialHour = initialLocalDateTime.hour,
        initialMinute = initialLocalDateTime.minute,
        is24Hour = true
    )
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateTime.toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds(),
        selectableDates = object : SelectableDates {
            override fun isSelectableYear(year: Int): Boolean {
                return year > 2022
            }
        }
    )

    // Date
    DateTimeDialog(
        modifier = modifier,
        isVisible = showDatePicker.value,
        properties = MultiplatformDialogProperties(desktopWindowConfig = dateDesktopWindowConfig),
        onCloseDialog = onCloseDialogs,
        shape = shape,
        onPositiveButtonClicked = {
            // Update Date
            datePickerState.selectedDateMillis?.let { millis ->
                dateTime = Instant.fromEpochMilliseconds(millis)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                onLocalDateTimeChange(dateTime)
            }
        },
        content = { DatePicker(state = datePickerState, showModeToggle = true) }
    )

    // Time
    DateTimeDialog(
        modifier = modifier,
        isVisible = showTimePicker.value,
        properties = MultiplatformDialogProperties(desktopWindowConfig = timeDesktopWindowConfig),
        onCloseDialog = onCloseDialogs,
        shape = shape,
        onPositiveButtonClicked = {
            // Update Time
            dateTime = LocalDateTime(
                dateTime.year,
                dateTime.monthNumber,
                dateTime.dayOfMonth,
                timePickerState.hour,
                timePickerState.minute,
            )
            onLocalDateTimeChange(dateTime)
        },
        content = { TimePicker(state = timePickerState) }
    )
}
