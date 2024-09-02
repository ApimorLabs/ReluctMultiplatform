package com.apimorlabs.reluct.domain.usecases.appInfo

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal class DesktopGetInstalledApps : GetInstalledApps {
    override suspend fun getApps(): ImmutableList<AppInfo> {
        return persistentListOf()
    }
}