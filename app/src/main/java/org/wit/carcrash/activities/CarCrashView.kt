package org.wit.carcrash.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.carcrash.R
import org.wit.carcrash.databinding.ActivityCarcrashBinding
import org.wit.carcrash.models.CarCrashModel
import timber.log.Timber.i


class CarCrashView : AppCompatActivity() {

    private lateinit var binding: ActivityCarcrashBinding
    private lateinit var presenter: CarCrashPresenter
    var carcrash = CarCrashModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityCarcrashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = CarCrashPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheCarCrash(carcrashTitle.text.toString(), description.text.toString())
            presenter.doSelectImage()
        }

        binding.carcrashLocation.setOnClickListener {
            presenter.cacheCarCrash(carcrashTitle.text.toString(), description.text.toString())
            presenter.doSetLocation()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_carcrash, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else {
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.carcrashTitle.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_carcrash_title, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    presenter.doAddOrSave(binding.carcrashTitle.text.toString(), binding.description.text.toString())
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun showCarCrash(carcrash: CarCrashModel) {
        binding.carcrashTitle.setText(carcrash.title)
        binding.description.setText(carcrash.description)

        Picasso.get()
            .load(carcrash.image)
            .into(binding.carcrashImage)
        if (carcrash.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_carcrash_image)
        }

    }

}