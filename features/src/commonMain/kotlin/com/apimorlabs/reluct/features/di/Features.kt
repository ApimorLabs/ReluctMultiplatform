package com.apimorlabs.reluct.features.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

object Features {
    fun KoinApplication.install() = apply {
        modules(sharedModule())
    }

    private fun sharedModule(): Module = module {
    }
}
