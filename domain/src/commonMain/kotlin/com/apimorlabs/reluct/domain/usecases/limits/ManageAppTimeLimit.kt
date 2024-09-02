package com.apimorlabs.reluct.domain.usecases.limits

import com.apimorlabs.reluct.common.models.domain.limits.AppTimeLimit
import kotlinx.coroutines.flow.Flow

interface ManageAppTimeLimit {
    /**
     * Get the specified app packageName time Limit as a Flow
     */
    fun getLimit(packageName: String): Flow<AppTimeLimit>

    /**
     * Get the specified app packageName time Limit synchronously
     */
    suspend fun getSync(packageName: String): AppTimeLimit

    suspend fun setTimeLimit(appTimeLimit: AppTimeLimit)
}
