package work.racka.template.common.di

import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import work.racka.template.common.sources.MyRepository
import work.racka.template.common.sources.MyRepositoryImpl
import work.racka.template.common.sources.MyViewModel

internal object Common {
    fun KoinApplication.install() = apply {
        modules(sharedModule(), platform())
    }

    private fun sharedModule() = module {
        //    single<MyRepository> {
        //        MyRepositoryImpl(dbClient = get())
        //    }
        // Can also be written as;
        singleOf(::MyRepositoryImpl).bind<MyRepository>()
        viewModelOf(::MyViewModel)
    }
}

internal expect fun platform(): Module
