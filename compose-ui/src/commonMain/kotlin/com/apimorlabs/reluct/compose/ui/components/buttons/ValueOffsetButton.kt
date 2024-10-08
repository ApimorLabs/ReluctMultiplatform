package com.apimorlabs.reluct.compose.ui.components.buttons

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateBefore
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.apimorlabs.reluct.compose.ui.theme.Dimens

/**
 * Increments integer values by 1
 */
@Composable
fun ValueOffsetButton(
    text: String,
    offsetValue: Int,
    onOffsetValueChange: (value: Int) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = LocalContentColor.current,
    shape: Shape = CircleShape,
    incrementEnabled: Boolean = true,
    decrementEnabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .animateContentSize()
                .padding(horizontal = Dimens.SmallPadding.size),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Decrement
            IconButton(
                onClick = { onOffsetValueChange(offsetValue - 1) },
                enabled = decrementEnabled
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.NavigateBefore,
                    contentDescription = "Decrease",
                    tint = contentColor.copy(alpha = if (decrementEnabled) 1f else .38f)
                )
            }

            // Middle Text
            Text(
                text = text,
                color = contentColor,
                style = textStyle
                    .copy(fontWeight = FontWeight.Medium),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Increment
            IconButton(
                onClick = { onOffsetValueChange(offsetValue + 1) },
                enabled = incrementEnabled
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
                    contentDescription = "Increase",
                    tint = contentColor.copy(alpha = if (decrementEnabled) 1f else .38f)
                )
            }
        }
    }
}
