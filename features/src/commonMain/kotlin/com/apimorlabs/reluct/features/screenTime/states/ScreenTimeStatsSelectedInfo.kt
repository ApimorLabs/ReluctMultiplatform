package com.apimorlabs.reluct.features.screenTime.states

data class ScreenTimeStatsSelectedInfo(
    val weekOffset: Int = 0,
    val selectedWeekText: String = "...",
    val selectedDay: Int = 0
)
