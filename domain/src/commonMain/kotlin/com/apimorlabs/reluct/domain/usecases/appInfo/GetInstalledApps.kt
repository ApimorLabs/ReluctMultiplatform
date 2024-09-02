package com.apimorlabs.reluct.domain.usecases.appInfo

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import kotlinx.collections.immutable.ImmutableList

interface GetInstalledApps {
    suspend fun getApps(): ImmutableList<AppInfo>
}
