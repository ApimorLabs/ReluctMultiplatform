package com.apimorlabs.reluct.domain.di

import com.apimorlabs.reluct.domain.usecases.appInfo.AndroidGetAppInfo
import com.apimorlabs.reluct.domain.usecases.appInfo.AndroidGetInstalledApps
import com.apimorlabs.reluct.domain.usecases.appInfo.GetAppInfo
import com.apimorlabs.reluct.domain.usecases.appInfo.GetInstalledApps
import com.apimorlabs.reluct.domain.usecases.tasks.AndroidManageTasksAlarms
import com.apimorlabs.reluct.domain.usecases.tasks.ManageTasksAlarms
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun platformModule(): Module = module {
    factory<GetAppInfo> {
        AndroidGetAppInfo(context = androidContext())
    }

    factory<GetInstalledApps> {
        AndroidGetInstalledApps(
            context = androidContext(),
            getAppInfo = get(),
            dispatcher = Dispatchers.IO
        )
    }

    factory<ManageTasksAlarms> {
        AndroidManageTasksAlarms(
            context = androidContext(),
            getTasks = get(),
            dispatcher = Dispatchers.IO
        )
    }
}
