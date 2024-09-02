package com.apimorlabs.reluct.domain.usecases.goals

import com.apimorlabs.reluct.common.models.domain.goals.Goal

interface ModifyGoals {
    suspend fun saveGoal(goal: Goal)
    suspend fun deleteGoal(id: String)
    suspend fun toggleGoalActiveState(isActive: Boolean, id: String)
    suspend fun updateGoalCurrentValue(id: String, currentValue: Long)
    suspend fun syncGoals()
}
