package com.apimorlabs.reluct.features.screenTime.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.theme.Dimens

@Composable
internal fun AppNameEntry(
    appName: String,
    icon: ImageBitmap,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
    contentPadding: Dp = Dimens.SmallPadding.size,
    contentAlignment: Alignment = Alignment.Center,
    iconSize: Dp = 32.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box(modifier = modifier, contentAlignment = contentAlignment) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            Image(
                modifier = Modifier.size(iconSize),
                bitmap = icon,
                contentDescription = appName
            )

            Text(
                modifier = Modifier.weight(1f),
                text = appName,
                style = textStyle,
                color = contentColor
            )

            actions()
        }
    }
}
