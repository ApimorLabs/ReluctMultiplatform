package com.apimorlabs.reluct.common.models.domain.goals

import kotlinx.collections.immutable.ImmutableList

data class Goal(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val relatedApps: ImmutableList<com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo>,
    val targetValue: Long,
    val currentValue: Long,
    val goalDuration: GoalDuration,
    val goalType: GoalType
)
