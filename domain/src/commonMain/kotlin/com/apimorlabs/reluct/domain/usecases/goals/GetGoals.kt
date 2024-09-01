package com.apimorlabs.reluct.domain.usecases.goals

import com.apimorlabs.reluct.common.models.domain.goals.Goal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface GetGoals {
    fun getActiveGoals(factor: Long, limitBy: Long = 10): Flow<ImmutableList<Goal>>
    fun getInActiveGoals(factor: Long, limitBy: Long = 10): Flow<ImmutableList<Goal>>
    fun getGoal(id: String): Flow<Goal?>
    suspend fun getGoalSync(id: String): Goal?
}
