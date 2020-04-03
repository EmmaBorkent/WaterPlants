package com.emmaborkent.waterplants.ui

import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.PlantDatabaseHandler
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_plant_details.*


class PlantDetailsActivity : AppCompatActivity() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var dbHandler: PlantDatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_details)
        setSupportActionBar(findViewById(R.id.include_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bottomSheetBehavior = BottomSheetBehavior.from(constraint_bottom_sheet)
        bottomSheet()

        // Get data from Intent and use ID to read info from database
        val plantID = intent.getIntExtra("PLANT_ID", 0)
        Log.d("INTENT", "Plant ID: $plantID")

        // Read plant from database
        dbHandler = PlantDatabaseHandler(this)
        val plant = dbHandler.readPlant(plantID)
        text_plant_name.text = plant.name
        text_plant_species.text = plant.species
        image_plant.setImageURI(Uri.parse(plant.image))
        // TODO: create function to change datePlantNeedsWater to amount of days
        text_water_in_days.text =
            this.getString(R.string.detail_water_in_days, plant.datePlantNeedsWater)
        text_mist_in_days.text =
            this.getString(R.string.detail_mist_in_days, plant.datePlantNeedsMist)
        text_water_every_days.text =
            this.getString(R.string.detail_water_repeat, plant.daysToNextWater)
        text_mist_every_days.text =
            this.getString(R.string.detail_mist_repeat, plant.daysToNextMist)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_detail_plant, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                // TODO set action on edit plant
                Toast.makeText(this, "Clicked Save", Toast.LENGTH_LONG).show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun bottomSheet() {
        // Get display height to calculate peekHeight for the Bottom Sheet
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        // TODO: This might go wrong with screens with different pixel densities, think of a fix
        val halfScreenHeight = displayMetrics.heightPixels * 0.41
        bottomSheetBehavior.peekHeight = halfScreenHeight.toInt()

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior
        .BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                // For state logging
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> Log.i("STATE", "Expanded State")
                    BottomSheetBehavior.STATE_COLLAPSED -> Log.i("STATE", "Collapsed State")
                    BottomSheetBehavior.STATE_DRAGGING -> Log.i("STATE", "Dragging...")
                    BottomSheetBehavior.STATE_SETTLING -> Log.i("STATE", "Settling...")
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> Log.i("STATE", "Half Expended State")
                    BottomSheetBehavior.STATE_HIDDEN -> Log.i("STATE", "Hidden State")
                }
            }
        })
    }
}
