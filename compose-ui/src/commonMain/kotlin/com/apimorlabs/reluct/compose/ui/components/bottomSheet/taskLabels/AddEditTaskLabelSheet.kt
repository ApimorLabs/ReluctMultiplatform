package com.apimorlabs.reluct.compose.ui.components.bottomSheet.taskLabels

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import com.apimorlabs.reluct.common.models.util.UUIDGen
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.bottomSheet.TopSheetSection
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.textFields.ReluctTextField
import com.apimorlabs.reluct.compose.ui.description_hint
import com.apimorlabs.reluct.compose.ui.discard_button_text
import com.apimorlabs.reluct.compose.ui.name_error_text
import com.apimorlabs.reluct.compose.ui.name_text
import com.apimorlabs.reluct.compose.ui.save_button_text
import com.apimorlabs.reluct.compose.ui.task_label_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.util.getContentColor
import com.apimorlabs.reluct.compose.ui.util.getRandomColor
import com.apimorlabs.reluct.compose.ui.util.toColor
import com.apimorlabs.reluct.compose.ui.util.toHexString
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddEditTaskLabelSheet(
    onClose: () -> Unit,
    initialLabel: TaskLabel?,
    onSaveLabel: (TaskLabel) -> Unit,
    onDeleteLabel: (TaskLabel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequest = LocalFocusManager.current

    val label = remember(initialLabel) {
        mutableStateOf(
            initialLabel ?: TaskLabel(
                id = UUIDGen.getString(),
                name = "",
                description = "",
                colorHexString = getRandomColor().toHexString()
            )
        )
    }

    val labelColors by remember(label.value.colorHexString) {
        derivedStateOf {
            val color = label.value.colorHexString.toColor()
            val contentColor = color.getContentColor()
            color to contentColor
        }
    }

    val labelContainer by animateColorAsState(targetValue = labelColors.first)
    val labelContent by animateColorAsState(targetValue = labelColors.second)

    var labelNameError by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
    ) {
        stickyHeader {
            TopSheetSection(
                sheetTitle = stringResource(Res.string.task_label_text),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onCloseClicked = onClose,
                rightButtonIcon = initialLabel?.let { Icons.Rounded.Delete },
                onRightButtonClicked = {
                    initialLabel?.let(onDeleteLabel)
                    onClose()
                }
            )
        }

        // Name
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
            ) {
                ReluctTextField(
                    modifier = Modifier.fillMaxWidth(.8f),
                    value = label.value.name,
                    hint = stringResource(Res.string.name_text),
                    isError = labelNameError,
                    errorText = stringResource(Res.string.name_error_text),
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
                        labelNameError = text.isBlank()
                        label.value = label.value.copy(name = text)
                    }
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(color = labelContainer, shape = Shapes.large),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        modifier = Modifier.padding(3.dp),
                        onClick = {
                            val colorHexString = getRandomColor().toHexString()
                            label.value = label.value.copy(colorHexString = colorHexString)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = "Random Color",
                            tint = labelContent
                        )
                    }
                }
            }
        }

        // Description
        item {
            ReluctTextField(
                modifier = Modifier
                    .height(200.dp),
                value = label.value.description,
                hint = stringResource(Res.string.description_hint),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                onTextChange = { text ->
                    label.value = label.value.copy(description = text)
                }
            )
        }

        // Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth().animateItem(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReluctButton(
                    buttonText = stringResource(Res.string.discard_button_text),
                    icon = Icons.Rounded.DeleteSweep,
                    onButtonClicked = onClose,
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
                        val isNameBlank = label.value.name.isBlank()
                        labelNameError = isNameBlank
                        if (!isNameBlank) {
                            onSaveLabel(label.value)
                            onClose()
                        }
                    }
                )
            }
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
