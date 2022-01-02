package org.wit.carcrash.views.carcrashlist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.carcrash.views.carcrash.CarCrashMapsView
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.CarCrashModel
import org.wit.carcrash.views.carcrash.CarCrashView

class CarCrashListPresenter(val view: CarCrashListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getCarCrashs() = app.carcrashs.findAll()

    fun doAddCarCrash() {
        val launcherIntent = Intent(view, CarCrashView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditCarCrash(carcrash: CarCrashModel) {
        val launcherIntent = Intent(view, CarCrashView::class.java)
        launcherIntent.putExtra("carcrash_edit", carcrash)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun doShowCarCrashsMap() {
        val launcherIntent = Intent(view, CarCrashMapsView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { getCarCrashs() }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}