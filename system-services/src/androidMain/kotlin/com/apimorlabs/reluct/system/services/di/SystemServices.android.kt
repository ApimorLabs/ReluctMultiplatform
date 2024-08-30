package com.apimorlabs.reluct.system.services.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import com.apimorlabs.reluct.system.services.haptics.AndroidHapticFeedback
import com.apimorlabs.reluct.system.services.haptics.HapticFeedback

internal actual fun platformModule(): Module = module {
    singleOf(::AndroidHapticFeedback).bind<HapticFeedback>()
}
