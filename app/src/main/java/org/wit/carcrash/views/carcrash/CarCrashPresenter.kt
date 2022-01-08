package org.wit.carcrash.views.carcrash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.carcrash.helpers.checkLocationPermissions
import org.wit.carcrash.helpers.createDefaultLocationRequest
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.Location
import org.wit.carcrash.models.CarCrashModel
import org.wit.carcrash.helpers.showImagePicker
import org.wit.carcrash.views.editLocation.EditLocationView
import timber.log.Timber
import timber.log.Timber.i

class CarCrashPresenter(private val view: CarCrashView) {

    private val locationRequest = createDefaultLocationRequest()
    var map: GoogleMap? = null
    var carcrash = CarCrashModel()
    var app: MainApp = view.application as MainApp
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)


    init {
        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        if (view.intent.hasExtra("carcrash-edit")) {
            edit = true
            carcrash = view.intent.extras?.getParcelable("carcrash_edit")!!
            view.showCarCrash(carcrash)
        }
        else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            carcrash.lat = location.lat
            carcrash.lng = location.lng
        }

    }

    suspend fun doAddOrSave(title: String, description: String) {
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

    suspend fun doDelete() {
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

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {

        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
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
        carcrash.zoom = 15f
        map?.clear()
        map?.uiSettings?.isZoomControlsEnabled = true
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
                        if(result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            carcrash.image = result.data!!.data
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
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }

}