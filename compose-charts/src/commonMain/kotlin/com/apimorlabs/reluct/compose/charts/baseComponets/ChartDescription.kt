package com.apimorlabs.reluct.compose.charts.baseComponets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
internal fun ChartDescription(
    chartColor: Color,
    chartName: String,
    descriptionStyle: TextStyle,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = chartColor)
                .size(8.dp)
        )
        Text(
            text = chartName,
            modifier = Modifier.padding(start = 10.dp),
            style = descriptionStyle
        )
    }
}
