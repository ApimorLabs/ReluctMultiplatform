package com.apimorlabs.reluct.compose.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
internal fun DateTimeDialog(
    onCloseDialog: () -> Unit,
    onPositiveButtonClicked: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    properties: MultiplatformDialogProperties = MultiplatformDialogProperties(
        desktopWindowConfig = DesktopWindowConfig(desktopWindowSize = DpSize(600.dp, 550.dp))
    ),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = RoundedCornerShape(10.dp),
    positiveButtonText: String? = "OK",
    negativeButtonText: String? = "CANCEL",
    content: @Composable () -> Unit = {},
) {
    MultiplatformDialog(
        onCloseDialog = onCloseDialog,
        isVisible = isVisible,
        modifier = modifier,
        properties = properties,
        content = {
            DialogContentHolder(
                positiveButtonClicked = onPositiveButtonClicked,
                negativeButtonClicked = onCloseDialog,
                content = content,
                containerColor = containerColor,
                shape = shape,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText
            )
        }
    )
}

@Composable
internal fun DialogContentHolder(
    positiveButtonClicked: () -> Unit,
    negativeButtonClicked: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = RoundedCornerShape(10.dp),
    positiveButtonText: String? = "OK",
    negativeButtonText: String? = "CANCEL",
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .wrapContentHeight()
            then modifier,
        shape = shape,
        color = containerColor
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            item { content() }

            item { Spacer(Modifier.height(8.dp)) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    if (negativeButtonText != null) {
                        val buttonText = negativeButtonText.uppercase(Locale.getDefault())
                        OutlinedButton(
                            onClick = negativeButtonClicked
                        ) {
                            Text(buttonText)
                        }
                        Spacer(Modifier.width(16.dp))
                    }
                    if (positiveButtonText != null) {
                        val buttonText = positiveButtonText.uppercase(Locale.getDefault())
                        Button(
                            onClick = positiveButtonClicked
                        ) {
                            Text(buttonText)
                        }
                    }
                }
            }
        }
    }
}
