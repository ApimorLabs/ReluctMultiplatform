package com.apimorlabs.reluct.compose.ui.components.topBar

import androidx.compose.foundation.layout.Arrangement
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
fun ReluctPageHeading(
    title: String,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    textAlign: TextAlign = TextAlign.Start,
    containerColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    shape: Shape = RectangleShape,
    tonalElevation: Dp = 0.dp,
    extraItems: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = Dimens.SmallPadding.size),
                text = title,
                style = titleTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = contentColor,
                textAlign = textAlign
            )

            extraItems()
        }
    }
}