package org.wit.carcrash.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.carcrash.R

import org.wit.carcrash.databinding.ActivityCarCrashMapsBinding
import org.wit.carcrash.databinding.ContentCarCrashMapsBinding
import org.wit.carcrash.main.MainApp

class CarCrashMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener  {

    private lateinit var binding: ActivityCarCrashMapsBinding
    private lateinit var contentBinding: ContentCarCrashMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityCarCrashMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        contentBinding = ContentCarCrashMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState);
        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    fun configureMap() {
        map.setOnMarkerClickListener(this)
        map.uiSettings.isZoomControlsEnabled = true
        app.carcrashs.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            map.addMarker(options).tag = it.id
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        val carcrash = app.carcrashs.findById(tag)
        currentTitle.text = carcrash!!.title
        currentDescription.text = carcrash!!.description
        imageView.setImageBitmap(readImageFromPath(this@CarCrashMapsActivity, carcrash.image))
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }


}