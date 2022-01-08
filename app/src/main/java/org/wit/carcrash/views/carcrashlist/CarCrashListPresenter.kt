package org.wit.carcrash.views.carcrashlist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.carcrash.views.carcrash.CarCrashMapsView
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.CarCrashModel
import org.wit.carcrash.views.carcrash.CarCrashView
import org.wit.carcrash.views.login.LoginView
import org.wit.carcrash.views.carcrashlist.CarCrashListView

class CarCrashListPresenter(val view: CarCrashListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerEditCallback()
        registerRefreshCallback()
    }

    suspend fun getCarCrashs() = app.carcrashs.findAll()

    fun doAddCarCrash() {
        val launcherIntent = Intent(view, CarCrashView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditCarCrash(carcrash: CarCrashModel) {
        val launcherIntent = Intent(view, CarCrashView::class.java)
        launcherIntent.putExtra("carcrash_edit", carcrash)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowCarCrashsMap() {
        val launcherIntent = Intent(view, CarCrashMapsView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main) {
                    getCarCrashs()
                }
            }
    }

    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }
    fun doLogout(){
        val launcherIntent = Intent(view, LoginView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }
}
