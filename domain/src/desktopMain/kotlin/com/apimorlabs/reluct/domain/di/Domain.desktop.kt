package com.apimorlabs.reluct.domain.di

import com.apimorlabs.reluct.domain.usecases.appInfo.DesktopGetAppInfo
import com.apimorlabs.reluct.domain.usecases.appInfo.DesktopGetInstalledApps
import com.apimorlabs.reluct.domain.usecases.appInfo.GetAppInfo
import com.apimorlabs.reluct.domain.usecases.appInfo.GetInstalledApps
import com.apimorlabs.reluct.domain.usecases.tasks.DesktopManageTasksAlarms
import com.apimorlabs.reluct.domain.usecases.tasks.ManageTasksAlarms
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual fun platformModule(): Module = module {
    factoryOf(::DesktopGetAppInfo).bind<GetAppInfo>()
    factoryOf(::DesktopGetInstalledApps).bind<GetInstalledApps>()
    factoryOf(::DesktopManageTasksAlarms).bind<ManageTasksAlarms>()
}
