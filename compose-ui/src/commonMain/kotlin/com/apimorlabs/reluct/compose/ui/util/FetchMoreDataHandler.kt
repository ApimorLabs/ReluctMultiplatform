package com.apimorlabs.reluct.compose.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

/**
 * Triggers [onFetchData] when [isFetchAllowedProvider] provides true. State that trigger fetching
 * should be captured in the [isFetchAllowedProvider] lambda and nowhere else.
 * This is defined as separate component to defer state reads and avoid recomposition
 */
@Composable
fun FetchMoreDataHandler(
    scrollContext: State<ScrollContext>,
    isFetchAllowedProvider: () -> Boolean,
    onFetchData: () -> Unit,
) {
    val isAtBottom = remember { derivedStateOf { scrollContext.value.isBottom } }
    LaunchedEffect(isAtBottom.value) {
        if (isFetchAllowedProvider() && isAtBottom.value) onFetchData()
    }
}
