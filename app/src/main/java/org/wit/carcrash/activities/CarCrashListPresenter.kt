package org.wit.carcrash.activities

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.CarCrashModel

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

    fun doEditCarCrash(placemark: CarCrashModel) {
        val launcherIntent = Intent(view, CarCrashView::class.java)
        launcherIntent.putExtra("carcrash_edit", carcrash)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun doShowCarCrashsMap() {
        val launcherIntent = Intent(view, CarCrashMapsActivity::class.java)
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
