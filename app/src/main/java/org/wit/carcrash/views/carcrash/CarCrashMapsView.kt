package org.wit.carcrash.views.carcrash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso

import org.wit.carcrash.databinding.ActivityCarCrashMapsBinding
import org.wit.carcrash.databinding.ContentCarCrashMapsBinding
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.CarCrashModel
import org.wit.carcrash.views.carcrashlist.CarCrashListPresenter

class CarCrashMapsView : AppCompatActivity(), GoogleMap.OnMarkerClickListener  {

    private lateinit var binding: ActivityCarCrashMapsBinding
    private lateinit var contentBinding: ContentCarCrashMapsBinding
    lateinit var app: MainApp
    lateinit var presenter: CarCrashListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityCarCrashMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = CarCrashMapPresenter(this)

        contentBinding = ContentCarCrashMapsBinding.bind(binding.root)

        contentBinding.mapView.onCreate(savedInstanceState);
        contentBinding.mapView.getMapAsync {
            presenter.doPopulateMap(it)
        }
    }

    fun showCarCrash(carcrash: CarCrashModel) {
        contentBinding.currentTitle.text = carcrash.title
        contentBinding.currentDescription.text = carcrash.description
        Picasso.get()
            .load(carcrash.image)
            .into(contentBinding.imageView2)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
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