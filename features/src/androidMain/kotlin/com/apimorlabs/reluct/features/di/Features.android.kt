package com.apimorlabs.reluct.features.di

import com.apimorlabs.reluct.features.screenTime.services.AndroidScreenTimeServices
import com.apimorlabs.reluct.features.screenTime.services.ScreenTimeServices
import com.apimorlabs.reluct.features.screenTime.work.ResumeAppsWork
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual fun platformModule(): Module = module {
    factoryOf(::AndroidScreenTimeServices).bind<ScreenTimeServices>()
    workerOf(::ResumeAppsWork)
}
