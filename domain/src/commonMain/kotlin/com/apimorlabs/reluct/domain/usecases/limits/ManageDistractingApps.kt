package com.apimorlabs.reluct.domain.usecases.limits

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface ManageDistractingApps {
    /**
     * Returns a [Flow] of pair of two Lists typed [AppInfo]
     * The first value of the pair is a list of Distracting apps
     * The second value of the pair is a list of Non Distracting Apps present in the user device
     */
    fun getApps(): Flow<Pair<ImmutableList<AppInfo>, ImmutableList<AppInfo>>>

    suspend fun markAsDistracting(packageName: String)

    suspend fun markAsNotDistracting(packageName: String)

    suspend fun isDistractingApp(packageName: String): Boolean
}
