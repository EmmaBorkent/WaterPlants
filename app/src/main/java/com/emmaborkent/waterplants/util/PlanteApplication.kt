package com.emmaborkent.waterplants.util

import android.app.Application
import timber.log.Timber

class PlanteApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}