package com.apimorlabs.reluct.domain.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

object Domain {
    fun KoinApplication.install() = apply {
        modules(sharedModule(), platformModule())
    }

    private fun sharedModule(): Module = module {
    }
}

internal expect fun platformModule(): Module
