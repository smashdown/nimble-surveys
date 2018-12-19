package com.nimble.surveys

import android.app.Application
import com.nimble.surveys.di.*
import com.nimble.surveys.utils.LineNumberDebugTree
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class SurveyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin(this, listOf(jsonModule, dataModule, networkModule, rxModule, viewModelModules))

        // Set a logger
        if (BuildConfig.DEBUG) Timber.plant(LineNumberDebugTree())
    }
}