package com.apimorlabs.reluct.screens.screentime.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.apimorlabs.reluct.common.models.domain.limits.AppTimeLimit
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.components.numberPicker.FullHours
import com.apimorlabs.reluct.compose.ui.components.numberPicker.Hours
import com.apimorlabs.reluct.compose.ui.components.numberPicker.HoursNumberPicker
import com.apimorlabs.reluct.compose.ui.save_button_text
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AppTimeLimitDialog(
    onDismiss: () -> Unit,
    initialAppTimeLimit: AppTimeLimit,
    onSaveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = modifier.size(300.dp),
            shape = Shapes.large,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = 6.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                var pickerValue by remember {
                    mutableStateOf<Hours>(
                        FullHours(
                            hours = initialAppTimeLimit.hours,
                            minutes = initialAppTimeLimit.minutes
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(Dimens.MediumPadding.size)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppNameEntry(
                        appName = initialAppTimeLimit.appInfo.appName,
                        icon = initialAppTimeLimit.appInfo.appIcon.icon.decodeToImageBitmap()
                    )

                    HoursNumberPicker(
                        value = pickerValue,
                        onValueChange = { pickerValue = it },
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

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        ReluctButton(
                            buttonText = stringResource(Res.string.save_button_text),
                            icon = Icons.Rounded.Done,
                            onButtonClicked = {
                                onSaveTimeLimit(pickerValue.hours, pickerValue.minutes)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}
