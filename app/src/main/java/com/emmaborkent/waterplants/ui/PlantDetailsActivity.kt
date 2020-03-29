package com.emmaborkent.waterplants.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.View
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
        val plantID = intent.getLongExtra("PLANT_ID", 0)
        Log.d("INTENT", "the item id is $plantID")

        // Read plant from database
//        dbHandler = PlantDatabaseHandler(this)
//        val plant = dbHandler.readPlant(plantID)
//        detail_plant_name.text = plant.name
//        detail_plant_species.text = plant.species
//        detail_plant_image.setImageURI(Uri.parse(plant.image))
//        detail_text_water_in_days.text.toString().format("%d", plant.endDate)
//        detail_text_spray_in_days.text.toString().format("%d", plant.endDate)
//        detail_text_water_repeat.text.toString().format("%d", plant.repeat)
//        detail_text_spray_repeat.text.toString().format("%d", plant.repeat)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_detail_plant, menu)
        return true
    }

    private fun bottomSheet() {
        // Get display height to calculate peekHeight for the Bottom Sheet
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        // This might go wrong with screens with different pixel densities
        val halfScreenHeight = displayMetrics.heightPixels*0.41
        bottomSheetBehavior.peekHeight = halfScreenHeight.toInt()

        bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior
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
