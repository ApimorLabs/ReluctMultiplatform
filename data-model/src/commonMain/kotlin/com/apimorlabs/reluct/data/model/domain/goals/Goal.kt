package com.apimorlabs.reluct.data.model.domain.goals

import kotlinx.collections.immutable.ImmutableList
import com.apimorlabs.reluct.data.model.domain.appInfo.AppInfo

data class Goal(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val relatedApps: ImmutableList<com.apimorlabs.reluct.data.model.domain.appInfo.AppInfo>,
    val targetValue: Long,
    val currentValue: Long,
    val goalDuration: GoalDuration,
    val goalType: GoalType
)
