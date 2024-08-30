package work.racka.reluct.system.services.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module

object SystemServices {

    fun KoinApplication.install() = apply {
        modules(platformModule())
    }
}

internal expect fun platformModule(): Module
