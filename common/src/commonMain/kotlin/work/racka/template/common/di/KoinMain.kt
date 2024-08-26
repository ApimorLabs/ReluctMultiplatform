package work.racka.template.common.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object KoinMain {
    fun init(
        appDeclaration: KoinAppDeclaration = {}
    ) {
        startKoin {
            appDeclaration()

            // Initialize modules
            Common.run { install() }
        }
    }
}
