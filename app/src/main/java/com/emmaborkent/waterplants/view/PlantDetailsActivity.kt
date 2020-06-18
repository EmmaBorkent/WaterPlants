package com.emmaborkent.waterplants.view

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.model.PLANT_ID
import com.emmaborkent.waterplants.viewmodel.PlantDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_plant_details.*

class PlantDetailsActivity : AppCompatActivity() {
    private val classNameTag: String = PlantDetailsActivity::class.java.simpleName
    private val viewModel: PlantDetailsViewModel by viewModels(
        factoryProducer = { SavedStateViewModelFactory(application, this) }
    )
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var plantID: Int = 0
    private lateinit var datePlantNeedsWater: String
    private lateinit var datePlantNeedsMist: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_details)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bottomSheetBehavior = BottomSheetBehavior.from(constraint_bottom_sheet)
        setBottomSheet()

        plantID = intent.getIntExtra(PLANT_ID, 0)
        SavedStateHandle().set(PLANT_ID, plantID)

//        val plantObserver = Observer<Plant> { plant ->
//            text_plant_name.text = plant.name
//            text_plant_species.text = plant.species
//            image_plant.setImageURI(Uri.parse(plant.image))
//            datePlantNeedsWater = plant.datePlantNeedsWater
//            datePlantNeedsMist = plant.datePlantNeedsMist
//            text_days_to_next_water.text = resources
//                .getQuantityString(
//                    R.plurals.detail_water_in_days,
//                    plant.daysToNextWater,
//                    plant.daysToNextWater
//                )
//            text_days_to_next_mist.text = resources
//                .getQuantityString(
//                    R.plurals.detail_mist_in_days,
//                    plant.daysToNextMist,
//                    plant.daysToNextMist
//                )
//            text_water_every_days.text = resources
//                .getQuantityString(
//                    R.plurals.detail_water_repeat,
//                    plant.waterEveryDays,
//                    plant.waterEveryDays
//                )
//            text_mist_every_days.text = resources
//                .getQuantityString(
//                    R.plurals.detail_mist_repeat,
//                    plant.mistEveryDays,
//                    plant.mistEveryDays
//                )
//
//            // TODO: 4-6-2020 Put functions in their own function
////            if (now() == plant.datePlantNeedsWater) {
////                button_give_water.setBackgroundResource(R.drawable.toggle_water_detail_active)
////                button_give_water.setOnClickListener {
////                    // TODO: 13-5-2020 Copied the checkbox code from adapter, so it probably needs to be somewhere else
////                    if (button_give_water.isChecked) {
////                        Log.d(classNameTag, "button.give.water isChecked")
////                        Plant().giveWater(plant)
////
////                        dbHandler.updatePlantInDatabase(plant)
////                    } else {
////                        Log.d(classNameTag, "button.give.water is not checked")
////                        Plant().undoWaterGift(plant)
////                        dbHandler.updatePlantInDatabase(plant)
////                    }
////                }
////            }
////            if (now() == datePlantNeedsMist) {
////                button_give_mist.setBackgroundResource(R.drawable.toggle_mist_detail_active)
////                button_give_mist.setOnClickListener {
////                    if (button_give_mist.isChecked) {
////                        Log.d(classNameTag, "button.give.mist isChecked")
////                        Plant().giveMist(plant)
////                        dbHandler.updatePlantInDatabase(plant)
////                    } else {
////                        Log.d(classNameTag, "button.give.mist is not checked")
////                        Plant().undoGiveMist(plant)
////                        dbHandler.updatePlantInDatabase(plant)
////                    }
////                }
////            }
//        }

//        viewModel.plant.observe(this, plantObserver)

//        setButtons()

    }

//    private fun setButtons() {
////        if (ParseFormatDates().getDefaultDateAsString() == datePlantNeedsWater)
//    }

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

    private fun setBottomSheet() {
        // Get display height to calculate peekHeight for the Bottom Sheet
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        // TODO: Setting the screen height this way might go wrong with screens with different pixel densities, think of a fix
        val halfScreenHeight = displayMetrics.heightPixels * 0.36
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
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> Log.i(
                        classNameTag,
                        "Half Expended State"
                    )
                    BottomSheetBehavior.STATE_HIDDEN -> Log.i(classNameTag, "Hidden State")
                }
            }
        })
    }
}