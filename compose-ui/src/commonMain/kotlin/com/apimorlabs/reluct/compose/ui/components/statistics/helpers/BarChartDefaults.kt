package com.apimorlabs.reluct.compose.ui.components.statistics.helpers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

object BarChartDefaults {

    val barColor
        @Composable get() = MaterialTheme.colorScheme.secondary.copy(alpha = .7f)
}
