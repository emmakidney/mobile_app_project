package org.wit.carcrash.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.carcrash.R
import org.wit.carcrash.databinding.ActivityCarcrashBinding
import org.wit.carcrash.main.MainApp
import org.wit.carcrash.models.Location
import org.wit.carcrash.models.CarCrashModel
import org.wit.carcrash.showImagePicker
import timber.log.Timber.i

class CarCrashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarcrashBinding
    var carcrash = CarCrashModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    var location = Location(52.245696, -7.139102, 15f)
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarcrashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        binding.carcrashLocation.setOnClickListener {
            val launcherIntent = Intent(this, MapsActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        app = application as MainApp

        i("CarCrash Activity started...")

        if (intent.hasExtra("carcrash_edit")) {
            edit = true
            carcrash = intent.extras?.getParcelable("carcrash_edit")!!
            binding.crashType.setOnCheckedChangeListener()
            binding.description.setText(carcrash.description)
            binding.btnAdd.setText(R.string.save_carcrash)
            Picasso.get()
                .load(carcrash.image)
                .into(binding.carcrashImage)
            if (carcrash.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_carcrash_image)
            }

        }

        binding.btnAdd.setOnClickListener() {
            carcrash.title = binding.crashType.checkedRadioButtonId.toString()
            carcrash.description = binding.description.text.toString()
            if (carcrash.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_carcrash_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.carcrashs.update(carcrash.copy())
                } else {
                    app.carcrashs.create(carcrash.copy())
                }
            }
            i("add Button Pressed: $carcrash")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_carcrash, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.carcrashs.delete(carcrash)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            carcrash.image = result.data!!.data!!
                            Picasso.get()
                                .load(carcrash.image)
                                .into(binding.carcrashImage)
                            binding.chooseImage.setText(R.string.change_carcrash_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }


    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            i("Location == $location")
                        } // end of if
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }
}


private fun RadioGroup.setOnCheckedChangeListener() {
    TODO("Not yet implemented")
}
