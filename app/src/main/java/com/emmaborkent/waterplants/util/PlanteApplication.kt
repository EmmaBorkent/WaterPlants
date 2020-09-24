package com.emmaborkent.waterplants.util

import android.app.Application
import timber.log.Timber

// Suppress Unused because this class is actually used in AndroidManifest.xml
@Suppress("unused")
class PlanteApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}