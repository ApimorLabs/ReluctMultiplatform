package com.apimorlabs.reluct.features.di

import com.apimorlabs.reluct.features.onboarding.OnBoardingViewModel
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object Features {
    fun KoinApplication.install() = apply {
        modules(sharedModule(), tasksModule(), goalsModule())
    }

    private fun sharedModule(): Module = module {
        // On Boarding
        viewModelOf(::OnBoardingViewModel)
    }
}
