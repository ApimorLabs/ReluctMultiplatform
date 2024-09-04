package com.apimorlabs.reluct.features.di

import com.apimorlabs.reluct.features.screenTime.services.DesktopScreenTimeServices
import com.apimorlabs.reluct.features.screenTime.services.ScreenTimeServices
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual fun platformModule(): Module = module {
    factoryOf(::DesktopScreenTimeServices).bind<ScreenTimeServices>()
}
