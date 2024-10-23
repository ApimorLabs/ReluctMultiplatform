package com.apimorlabs.reluct

import android.app.Application
import androidx.work.WorkManager
import com.apimorlabs.reluct.common.di.KoinMain
import com.apimorlabs.reluct.features.screenTime.services.ScreenTimeServices
import com.apimorlabs.reluct.features.screenTime.work.ResumeAppsWork
import com.apimorlabs.reluct.features.settings.GetSettings
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.logger.Level

class ReluctApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinMain.init {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ReluctApplication)
            workManagerFactory()
        }

        // Enable once Services have been fixed and Work Manager is set up
        val scope = MainScope()
        scope.launch {
            val settings = get<GetSettings>()
            if (settings.getOnBoardingShow()) {
                val screenTimeServices = get<ScreenTimeServices>()
                screenTimeServices.startLimitsService()
            }
        }.invokeOnCompletion { scope.cancel() }

        // Setup Resume Apps worker
        ResumeAppsWork.run {
            WorkManager.getInstance(this@ReluctApplication).scheduleWork()
        }
    }
}
