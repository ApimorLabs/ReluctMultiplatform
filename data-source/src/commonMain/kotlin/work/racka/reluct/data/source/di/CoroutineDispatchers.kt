package work.racka.reluct.data.source.di

import kotlinx.coroutines.CoroutineDispatcher

expect object CoroutineDispatchers {
    val background: CoroutineDispatcher
}
