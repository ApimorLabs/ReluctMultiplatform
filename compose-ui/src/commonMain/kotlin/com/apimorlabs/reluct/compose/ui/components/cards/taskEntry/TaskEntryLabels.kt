package com.apimorlabs.reluct.compose.ui.components.cards.taskEntry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.checkboxes.RoundCheckbox
import com.apimorlabs.reluct.compose.ui.in_time_text
import com.apimorlabs.reluct.compose.ui.overdue_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskDetailsHeading(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(Dimens.SmallPadding.size)
    ) {
        RoundCheckbox(
            isChecked = isChecked,
            onCheckedChange = {
                onCheckedChange(it)
            }
        )
        Text(
            text = text,
            style = textStyle,
            color = LocalContentColor.current
        )
    }
}

@Composable
internal fun TaskTimeInfo(
    timeText: String,
    modifier: Modifier = Modifier,
    showOverdueLabel: Boolean = false,
    overdue: Boolean = false,
) {
    Row(modifier = modifier) {
        Text(
            text = timeText,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = LocalContentColor.current.copy(alpha = .8f)
        )

        if (showOverdueLabel) {
            Spacer(modifier = Modifier.width(Dimens.ExtraSmallPadding.size))
            Text(
                text = "-",
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(alpha = .8f)
            )
            Spacer(modifier = Modifier.width(Dimens.ExtraSmallPadding.size))
            Text(
                text = if (overdue) {
                    stringResource(Res.string.overdue_text)
                } else {
                    stringResource(Res.string.in_time_text)
                },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (overdue) MaterialTheme.colorScheme.error else Color(0xFF23B33A)
            )
        }
    }
}

/*
@Preview
@Composable
internal fun LabelsPreview() {
    ReluctAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val isChecked = remember { mutableStateOf(false) }
            Column {
                EntryHeading(text = "Tasks Title Here")
                Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding.size))
                EntryDescription(
                    text = """
                        This is a task description and it it really long. This is a task
                        description and it it really long. This is a task description and it
                        it really long. This is a task description and it it really long.
                        This is a task description and it it really long.
                    """.trimIndent()
                )
                Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding.size))
                TaskTimeInfo(timeText = "In 3 hrs")
                TaskTimeInfo(timeText = "Thu, 20 Feb", showOverdueLabel = true)
                TaskTimeInfo(timeText = "Thu, 20 Feb", showOverdueLabel = true, overdue = true)
                Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding.size))
                RoundCheckbox(
                    onCheckedChange = {
                        isChecked.value = it
                        Logger.d("IsChecked: $it")
                    },
                    isChecked = isChecked.value
                )
            }
        }
    }
}*/
