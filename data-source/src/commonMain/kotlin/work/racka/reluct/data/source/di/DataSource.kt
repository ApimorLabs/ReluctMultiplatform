package work.racka.reluct.data.source.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

object DataSource {
    fun KoinApplication.install() = apply {
        modules(sharedModule(), platformModule())
    }

    private fun sharedModule(): Module = module {
    }
}

internal expect fun platformModule(): Module