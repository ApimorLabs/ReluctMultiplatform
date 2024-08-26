package work.racka.reluct

import android.app.Application
import org.koin.android.ext.koin.androidContext
import work.racka.reluct.common.di.KoinMain

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinMain.init {
            androidContext(this@MyApplication)
        }
    }
}
