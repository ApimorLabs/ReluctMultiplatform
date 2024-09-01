package com.apimorlabs.reluct.domain.usecases.limits

import com.apimorlabs.reluct.common.models.domain.limits.AppLimits
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface GetPausedApps {
    fun getApps(): Flow<ImmutableList<AppLimits>>

    suspend fun getSync(): ImmutableList<AppLimits>

    suspend fun isPaused(packageName: String): Boolean
}
