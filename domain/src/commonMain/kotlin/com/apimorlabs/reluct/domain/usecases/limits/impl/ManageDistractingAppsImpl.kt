package com.apimorlabs.reluct.domain.usecases.limits.impl

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import com.apimorlabs.reluct.common.models.util.filterPersistentNot
import com.apimorlabs.reluct.domain.usecases.appInfo.GetInstalledApps
import com.apimorlabs.reluct.domain.usecases.limits.GetDistractingApps
import com.apimorlabs.reluct.domain.usecases.limits.ManageDistractingApps
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

internal class ManageDistractingAppsImpl(
    private val getDistractingApps: GetDistractingApps,
    private val getInstalledApps: GetInstalledApps,
    private val modifyAppLimits: ModifyAppLimits,
    private val haptics: HapticFeedback,
    private val backgroundDispatcher: CoroutineDispatcher
) : ManageDistractingApps {

    private var installedApps: ImmutableList<AppInfo> = persistentListOf()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getApps(): Flow<Pair<ImmutableList<AppInfo>, ImmutableList<AppInfo>>> =
        getDistractingApps.getApps().mapLatest { distractingApps ->
            if (installedApps.isEmpty()) installedApps = getInstalledApps.getApps()
            val distractingAppsInfo = distractingApps.map { it.appInfo }.toImmutableList()
            val nonDistractingApps = installedApps.filterPersistentNot { installed ->
                distractingAppsInfo.any { it.packageName == installed.packageName }
            }
            Pair(first = distractingAppsInfo, second = nonDistractingApps)
        }.flowOn(backgroundDispatcher)

    override suspend fun markAsDistracting(packageName: String) {
        modifyAppLimits.makeDistractingApp(packageName = packageName, isDistracting = true)
        haptics.tick()
    }

    override suspend fun markAsNotDistracting(packageName: String) {
        modifyAppLimits.makeDistractingApp(packageName = packageName, isDistracting = false)
        haptics.tick()
    }

    override suspend fun isDistractingApp(packageName: String): Boolean =
        getDistractingApps.isDistractingApp(packageName)
}
