package com.emmaborkent.waterplants.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.PlantDatabaseHandler
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_plant_details.*

class PlantDetailsActivity : AppCompatActivity() {
    private val classNameTag: String = PlantDetailsActivity::class.java.simpleName
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var dbHandler: PlantDatabaseHandler
    private var plantID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_details)
        setSupportActionBar(findViewById(R.id.include_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bottomSheetBehavior = BottomSheetBehavior.from(constraint_bottom_sheet)
        bottomSheet()

        // Get data from Intent and use ID to read info from database
        plantID = intent.getIntExtra("PLANT_ID", 0)
        Log.d(classNameTag, "Receiving Intent from MainActivity. Plant ID is: $plantID")

        // Read plant from database
        dbHandler = PlantDatabaseHandler(this)
        val plant = dbHandler.readPlant(plantID)
        text_plant_name.text = plant.name
        text_plant_species.text = plant.species
        image_plant.setImageURI(Uri.parse(plant.image))
        // TODO: create a function to change datePlantNeedsWater to amount of days, don't forget to
        //  put quantity in twice
        text_water_in_days.text = resources
            .getQuantityString(R.plurals.detail_water_in_days, 1, 1)
        text_mist_in_days.text = resources
            .getQuantityString(R.plurals.detail_mist_in_days, 2, 2)
        val waterEveryDays = plant.daysToNextWater.toInt()
        text_water_every_days.text = resources
            .getQuantityString(R.plurals.detail_water_repeat, waterEveryDays, waterEveryDays)
        val mistEveryDays = plant.daysToNextMist.toInt()
        text_mist_every_days.text = resources
            .getQuantityString(R.plurals.detail_mist_repeat, mistEveryDays, mistEveryDays)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_detail_plant, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                val editPlantIntent = Intent(this, AddEditPlantActivity::class.java)
                Log.d(classNameTag, "Going to AddEditPlantActivity. Plant ID is: $plantID")
                editPlantIntent.putExtra("PLANT_ID", plantID)
                startActivity(editPlantIntent)
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
        // TODO: Setting the screen height this way might go wrong with screens with different pixel densities, think of a fix
        val halfScreenHeight = displayMetrics.heightPixels * 0.41
        bottomSheetBehavior.peekHeight = halfScreenHeight.toInt()

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior
        .BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> Log.i(classNameTag, "Expanded State")
                    BottomSheetBehavior.STATE_COLLAPSED -> Log.i(classNameTag, "Collapsed State")
                    BottomSheetBehavior.STATE_DRAGGING -> Log.i(classNameTag, "Dragging...")
                    BottomSheetBehavior.STATE_SETTLING -> Log.i(classNameTag, "Settling...")
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> Log.i(classNameTag, "Half Expended State")
                    BottomSheetBehavior.STATE_HIDDEN -> Log.i(classNameTag, "Hidden State")
                }
            }
        })
    }
}
