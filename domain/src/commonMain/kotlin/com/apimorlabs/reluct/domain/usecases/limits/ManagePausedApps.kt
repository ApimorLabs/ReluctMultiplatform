package com.apimorlabs.reluct.domain.usecases.limits

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface ManagePausedApps {
    /**
     * Returns a [Flow] of pair of two Lists typed [AppInfo]
     * The first value of the pair is a list of Paused apps
     * The second value of the pair is a list of Non Paused Apps present in the user device
     */
    fun getApps(): Flow<Pair<ImmutableList<AppInfo>, ImmutableList<AppInfo>>>

    suspend fun pauseApp(packageName: String)
    suspend fun unPauseApp(packageName: String)
    suspend fun isPaused(packageName: String): Boolean
}
