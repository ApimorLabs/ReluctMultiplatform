package di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object KoinMain {
    fun initKoin(
        appDeclaration: KoinAppDeclaration = {}
    ) {
        startKoin {
            appDeclaration()

            // Initialize Modules
            modules(platformModule, sharedModule)
        }
    }
}