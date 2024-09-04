package com.apimorlabs.reluct.features.screenTime.states

import com.apimorlabs.reluct.common.models.domain.limits.AppTimeLimit

sealed class AppTimeLimitState {
    data object Nothing : AppTimeLimitState()
    data object Loading : AppTimeLimitState()
    data class Data(val timeLimit: AppTimeLimit) : AppTimeLimitState()
}