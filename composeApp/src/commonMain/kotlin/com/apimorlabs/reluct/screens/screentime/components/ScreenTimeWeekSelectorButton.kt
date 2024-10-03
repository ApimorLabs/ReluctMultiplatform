package com.apimorlabs.reluct.screens.screentime.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.apimorlabs.reluct.compose.ui.components.buttons.ValueOffsetButton
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import com.apimorlabs.reluct.features.screenTime.states.ScreenTimeStatsSelectedInfo

@Composable
internal fun ScreenTimeWeekSelectorButton(
    selectedInfoProvider: () -> ScreenTimeStatsSelectedInfo,
    onUpdateWeekOffset: (offset: Int) -> Unit
) {
    val selectedInfo = remember { derivedStateOf { selectedInfoProvider() } }
    ValueOffsetButton(
        text = selectedInfo.value.selectedWeekText,
        offsetValue = selectedInfo.value.weekOffset,
        onOffsetValueChange = onUpdateWeekOffset,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = Shapes.large,
        incrementEnabled = selectedInfo.value.weekOffset < 0
    )
}
