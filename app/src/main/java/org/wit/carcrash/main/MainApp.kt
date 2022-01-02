package org.wit.carcrash.main

import android.app.Application
import org.wit.carcrash.models.CarCrashJSONStore
import org.wit.carcrash.models.CarCrashStore
import org.wit.carcrash.room.CarCrashStoreRoom
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var carcrashs: CarCrashStore

    override fun onCreate() {
        super.onCreate()
        carcrashs = CarCrashStoreRoom(applicationContext)
        i("CarCrash started")
    }
}