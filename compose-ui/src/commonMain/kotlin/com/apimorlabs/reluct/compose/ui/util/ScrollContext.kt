package com.apimorlabs.reluct.compose.ui.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

private fun LazyListState.isLastItemVisible(): Boolean =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

private fun LazyListState.isFirstItemVisible(): Boolean =
    firstVisibleItemIndex == 0

data class ScrollContext(
    val isTop: Boolean,
    val isBottom: Boolean,
)

// TODO1: Remove this and find a different way to make collapsible toolbar
@Composable
fun rememberScrollContext(listState: LazyListState): State<ScrollContext> = remember {
    derivedStateOf {
        ScrollContext(
            isTop = listState.isFirstItemVisible(),
            isBottom = listState.isLastItemVisible()
        )
    }
}
