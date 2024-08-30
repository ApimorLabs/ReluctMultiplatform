package com.apimorlabs.reluct.system.services.di

import com.apimorlabs.reluct.system.services.haptics.DesktopHapticFeedback
import com.apimorlabs.reluct.system.services.haptics.HapticFeedback
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual fun platformModule(): Module = module {
    singleOf(::DesktopHapticFeedback).bind<HapticFeedback>()
}
