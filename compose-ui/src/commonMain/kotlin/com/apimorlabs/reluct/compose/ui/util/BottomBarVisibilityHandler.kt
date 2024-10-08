package com.apimorlabs.reluct.compose.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

/**
 * A visibility handler that decides when to hide or show the bottom bar
 * This is defined as separate component to defer state reads and avoid recomposition
 */
@Composable
fun BottomBarVisibilityHandler(
    scrollContext: State<ScrollContext>,
    barsVisibility: BarsVisibility,
) {
    if (scrollContext.value.isTop) {
        barsVisibility.bottomBar.show()
    } else {
        barsVisibility.bottomBar.hide()
    }
}
