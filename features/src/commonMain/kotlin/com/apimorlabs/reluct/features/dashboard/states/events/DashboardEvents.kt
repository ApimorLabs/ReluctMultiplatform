package com.apimorlabs.reluct.features.dashboard.states.events

sealed class DashboardEvents {
    data object Nothing : DashboardEvents()
    data class ShowMessageDone(val isDone: Boolean, val msg: String) : DashboardEvents()
}
