package org.wit.carcrash.views.carcrashlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import org.wit.carcrash.R
import org.wit.carcrash.databinding.ActivityCarcrashListBinding
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.CarCrashModel
import timber.log.Timber.i

class CarCrashListView : AppCompatActivity(), CarCrashListener/*, MultiplePermissionsListener*/ {

    lateinit var app: MainApp
    private lateinit var binding: ActivityCarcrashListBinding
    lateinit var presenter: CarCrashListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarcrashListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        val user = FirebaseAuth.getInstance().currentUser
        if (user!= null) {
            binding.toolbar.title = "${title}: ${user.email}"
        }

        setSupportActionBar(binding.toolbar)
        presenter = CarCrashListPresenter(this)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        updateRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        //update the view
        super.onResume()
        updateRecyclerView()
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("recyclerView onResume")
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


    private fun updateRecyclerView() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.recyclerView.adapter =
                CarCrashAdapter(presenter.getCarCrashs(), this@CarCrashListView)
        }
    }

}