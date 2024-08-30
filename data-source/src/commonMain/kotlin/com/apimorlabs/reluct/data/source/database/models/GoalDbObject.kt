package com.apimorlabs.reluct.data.source.database.models

import com.apimorlabs.reluct.data.model.domain.goals.GoalInterval
import com.apimorlabs.reluct.data.model.domain.goals.GoalType
import com.apimorlabs.reluct.data.model.util.time.Week

typealias PackageName = String

data class GoalDbObject(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val relatedApps: List<PackageName>,
    val targetValue: Long,
    val currentValue: Long,
    val goalInterval: GoalInterval,
    val timeInterval: LongRange?,
    val daysOfWeekSelected: List<Week>,
    val goalType: GoalType
)
