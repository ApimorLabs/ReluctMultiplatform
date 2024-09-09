package com.apimorlabs.reluct.compose.ui.components.topBar.toolbar

abstract class ScrollFlagState(heightRange: IntRange) : ToolbarState {
    init {
        require(heightRange.first >= 0 && heightRange.last >= heightRange.first) {
            "The lowest height value must be >= 0 and the highest height value must be >= the lowest value."
        }
    }

    protected val minHeight = heightRange.first
    protected val maxHeight = heightRange.last
    protected val rangeDifference = maxHeight - minHeight
    protected var consumedProtect: Float = 0f

    protected abstract var scrollOffsetProtect: Float

    final override val height: Float
        get() = (maxHeight - scrollOffset).coerceIn(minHeight.toFloat(), maxHeight.toFloat())

    final override val progress: Float
        get() = 1 - (maxHeight - height) / rangeDifference

    final override val consumed: Float
        get() = consumedProtect

    final override var scrollTopLimitReached: Boolean = true
}

abstract class FixedScrollFlagState(heightRange: IntRange) : ScrollFlagState(heightRange) {
    final override val offset: Float = 0f
}
