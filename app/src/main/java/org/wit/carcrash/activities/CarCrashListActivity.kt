package org.wit.carcrash.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.carcrash.R
import org.wit.carcrash.adapters.CarCrashAdapter
import org.wit.carcrash.adapters.CarCrashListener
import org.wit.carcrash.databinding.ActivityCarcrashListBinding
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.CarCrashModel

class CarCrashListActivity : AppCompatActivity(), CarCrashListener/*, MultiplePermissionsListener*/ {

    lateinit var app: MainApp
    private lateinit var binding: ActivityCarcrashListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarcrashListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadCarCrashs()
        registerRefreshCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, CarCrashActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, CarCrashMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCarCrashClick(carcrash: CarCrashModel) {
        val launcherIntent = Intent(this, CarCrashActivity::class.java)
        launcherIntent.putExtra("carcrash_edit", carcrash)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadCarCrashs() }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

    private fun loadCarCrashs() {
        showCarCrashs(app.carcrashs.findAll())
    }

    fun showCarCrashs (carcrashs: List<CarCrashModel>) {
        binding.recyclerView.adapter = CarCrashAdapter(carcrashs, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}