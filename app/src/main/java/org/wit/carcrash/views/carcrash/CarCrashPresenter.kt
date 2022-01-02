package org.wit.carcrash.views.carcrash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.carcrash.helpers.checkLocationPermissions
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.Location
import org.wit.carcrash.models.CarCrashModel
import org.wit.carcrash.showImagePicker
import org.wit.carcrash.views.editLocation.EditLocationView
import timber.log.Timber
import timber.log.Timber.i

class CarCrashPresenter(private val view: CarCrashView) {

    var map: GoogleMap? = null
    var carcrash = CarCrashModelModel()
    var app: MainApp = view.application as MainApp
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)


    init {

        registerImagePickerCallback()
        registerMapCallback()
        doPermissionLauncher()

        if (view.intent.hasExtra("carcrash-edit")) {
            edit = true
            carcrash = view.intent.extras?.getParcelable("carcrash_edit")!!
            view.showCarCrash(carcrash)
        }
        else {
            if (checkLocationPermissions(view)) {
                //
            }
            carcrash.lat = location.lat
            carcrash.lng = location.lng
        }

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

        if (carcrash.zoom != 0f) {
            location.lat =  carcrash.lat
            location.lng = carcrash.lng
            location.zoom = carcrash.zoom
            locationUpdate(carcrash.lat, carcrash.lng)
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheCarCrash (title: String, description: String) {
        carcrash.title = title;
        carcrash.description = description
    }

    fun doConfigureMap(m: GoogleMap){
        map = m
        locationUpdate(carcrash.lat, carcrash.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        carcrash.lat = lat
        carcrash.lng = lng
        carcrash.zoom =  15f
        map?.clear()
        map?.uiSettings?.isZoomControlsEnabled(true)
        val options = MarkerOptions().title(carcrash.title).position(LatLng(carcrash.lat, carcrash.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(carcrash.lat, carcrash.lng), carcrash.zoom))
        view.showCarCrash(carcrash)
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

    private fun doPermissionLauncher() {
        i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                   // doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }

}