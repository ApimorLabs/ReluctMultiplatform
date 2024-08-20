package org.example.project

import android.app.Application
import di.KoinMain
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinMain.initKoin {
            androidContext(this@MyApplication)
        }
    }
}