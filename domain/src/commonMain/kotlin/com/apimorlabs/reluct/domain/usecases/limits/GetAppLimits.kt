package com.apimorlabs.reluct.domain.usecases.limits

import com.apimorlabs.reluct.common.models.domain.limits.AppLimits
import kotlinx.coroutines.flow.Flow

interface GetAppLimits {
    fun getApp(packageName: String): Flow<AppLimits>
    suspend fun getAppSync(packageName: String): AppLimits
}
