package com.apimorlabs.reluct.compose.ui.components.cards.taskLabelEntry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.compose.ui.util.getContentColor
import com.apimorlabs.reluct.compose.ui.util.toColor

@Composable
fun TaskLabelPill(
    name: String,
    colorHex: String,
    modifier: Modifier = Modifier,
) {
    val labelColors by remember(colorHex) {
        derivedStateOf {
            val color = colorHex.toColor()
            val contentColor = color.getContentColor()
            color to contentColor
        }
    }

    Box(
        modifier = Modifier
            .clip(Shapes.large)
            .background(labelColors.first) then modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = Dimens.SmallPadding.size,
                horizontal = Dimens.MediumPadding.size
            ),
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = labelColors.second,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
