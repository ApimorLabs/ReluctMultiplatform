package com.apimorlabs.reluct.data.model.domain.goals

import com.apimorlabs.reluct.data.model.domain.appInfo.AppInfo
import kotlinx.collections.immutable.ImmutableList

data class Goal(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val relatedApps: ImmutableList<AppInfo>,
    val targetValue: Long,
    val currentValue: Long,
    val goalDuration: GoalDuration,
    val goalType: GoalType
)
