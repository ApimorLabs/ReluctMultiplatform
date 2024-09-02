package com.apimorlabs.reluct.domain.usecases.limits.impl

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import com.apimorlabs.reluct.common.models.util.filterPersistentNot
import com.apimorlabs.reluct.domain.usecases.appInfo.GetInstalledApps
import com.apimorlabs.reluct.domain.usecases.limits.GetPausedApps
import com.apimorlabs.reluct.domain.usecases.limits.ManagePausedApps
import com.apimorlabs.reluct.domain.usecases.limits.ModifyAppLimits
import com.apimorlabs.reluct.system.services.haptics.HapticFeedback
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

internal class ManagePausedAppsImpl(
    private val getPausedApps: GetPausedApps,
    private val getInstalledApps: GetInstalledApps,
    private val modifyAppLimits: ModifyAppLimits,
    private val haptics: HapticFeedback,
    private val backgroundDispatcher: CoroutineDispatcher
) : ManagePausedApps {

    private var installedApps: ImmutableList<AppInfo> = persistentListOf()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getApps(): Flow<Pair<ImmutableList<AppInfo>, ImmutableList<AppInfo>>> =
        getPausedApps.getApps().mapLatest { pausedApps ->
            if (installedApps.isEmpty()) installedApps = getInstalledApps.getApps()
            val pausedAppsInfo = pausedApps.map { it.appInfo }.toImmutableList()
            val nonPausedApps = installedApps.filterPersistentNot { installed ->
                pausedAppsInfo.any { it.packageName == installed.packageName }
            }
            Pair(first = pausedAppsInfo, second = nonPausedApps)
        }.flowOn(backgroundDispatcher)

    override suspend fun pauseApp(packageName: String) {
        modifyAppLimits.pauseApp(packageName = packageName, isPaused = true)
        haptics.tick()
    }

    override suspend fun unPauseApp(packageName: String) {
        modifyAppLimits.pauseApp(packageName = packageName, isPaused = false)
        haptics.tick()
    }

    override suspend fun isPaused(packageName: String): Boolean =
        getPausedApps.isPaused(packageName)
}
