package com.apimorlabs.reluct.compose.ui.components.cards.headers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.theme.Dimens

@Composable
fun ListGroupHeadingHeader(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable RowScope.() -> Unit = {},
    trailingIcon: @Composable RowScope.() -> Unit = {},
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    textAlign: TextAlign = TextAlign.Start,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    shape: Shape = RectangleShape,
    tonalElevation: Dp = 0.dp
) {
    Surface(
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                leadingIcon()

                Text(
                    modifier = modifier
                        .weight(1f)
                        .padding(Dimens.SmallPadding.size),
                    text = text,
                    style = textStyle,
                    textAlign = textAlign,
                    overflow = TextOverflow.Ellipsis,
                    color = contentColor
                )

                trailingIcon()
            }
        }
    }
}