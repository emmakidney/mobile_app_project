package org.wit.carcrash.activities

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import org.wit.carcrash.R
import org.wit.carcrash.databinding.ActivityCarcrashBinding
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.Location
import org.wit.carcrash.models.CarCrashModel
import org.wit.carcrash.showImagePicker
import org.wit.carcrash.views.location.EditLocationView
import timber.log.Timber

class CarCrashPresenter(private val view: CarCrashView) {

    var placemark = CarCrashModelModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityCarcrashBinding = ActivityCarcrashBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false;

    init {
        if (view.intent.hasExtra("carcrash-edit")) {
            edit = true
            carcrash = view.intent.extras?.getParcelable("carcrash_edit")!!
            view.showCarCrash(carcrash)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    fun doAddOrSave(title: String, description: String) {
        carcrash.title = title
        carcrash.description = description
        if (edit) {
            app.carcrashs.update(carcrash)
        } else {
            app.carcrashs.create(carcrash)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    fun doDelete() {
        app.carcrashs.delete(carcrash)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (carcrash.zoom != 0f) {
            location.lat =  carcrash.lat
            location.lng = carcrash.lng
            location.zoom = carcrash.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }
    fun cacheCarCrash(title: String, description: String) {
        carcrash.title = title;
        carcrash.description = description
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            carcrash.image = result.data!!.data!!
                            view.updateImage(carcrash.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            carcrash.lat = location.lat
                            carcrash.lng = location.lng
                            carcrash.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }
}