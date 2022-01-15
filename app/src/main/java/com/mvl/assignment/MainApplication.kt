package com.mvl.assignment

import android.app.Application
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        // plant timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

fun enableLogging() = BuildConfig.BUILD_TYPE != "release"
