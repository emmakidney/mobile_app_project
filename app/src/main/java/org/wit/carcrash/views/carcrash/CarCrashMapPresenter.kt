package org.wit.carcrash.views.carcrash

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.carcrash.main.MainApp

class CarCrashMapPresenter(val view: CarCrashMapsView) {
    var app: MainApp

    init {
        app = view.application as MainApp
    }

    suspend fun doPopulateMap(map: GoogleMap) {
        map.uiSettings.setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(view)
        app.carcrashs.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    suspend fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        val carcrash = app.carcrashs.findById(tag)
        if (carcrash != null) view.showCarCrash(carcrash)
    }
}