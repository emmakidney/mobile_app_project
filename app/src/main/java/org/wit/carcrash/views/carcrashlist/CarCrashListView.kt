package org.wit.carcrash.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.carcrash.R
import org.wit.carcrash.adapters.CarCrashAdapter
import org.wit.carcrash.adapters.CarCrashListener
import org.wit.carcrash.databinding.ActivityCarcrashListBinding
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.CarCrashModel
import org.wit.carcrash.views.carcrashlist.CarCrashListPresenter

class CarCrashListActivity : AppCompatActivity(), CarCrashListener/*, MultiplePermissionsListener*/ {

    lateinit var app: MainApp
    private lateinit var binding: ActivityCarcrashListBinding
    lateinit var presenter: CarCrashListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarcrashListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = CarCrashListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadCarCrashs()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddCarCrash() }
            R.id.item_map -> { presenter.doShowCarCrashsMap() }
            R.id.item_logout -> { presenter.doLogout() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCarCrashClick(carcrash: CarCrashModel) {
        presenter.doEditCarCrash(carcrash)
    }


    private fun loadCarCrashs() {
        binding.recyclerView.adapter = CarCrashAdapter(presenter.getCarCrashs(), this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        //update the view
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("recyclerView onResume")
        super.onResume()
    }


}