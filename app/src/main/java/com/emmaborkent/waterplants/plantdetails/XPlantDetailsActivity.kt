package com.emmaborkent.waterplants.plantdetails

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.addeditplant.XAddEditPlantActivity
import com.emmaborkent.waterplants.databinding.ActivityPlantDetailsBinding
import com.emmaborkent.waterplants.PlantViewModel
import com.emmaborkent.waterplants.model.Plant
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_plant_details.*

class XPlantDetailsActivity : AppCompatActivity() {
    private val classNameTag: String = XPlantDetailsActivity::class.java.simpleName

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
//    private val plantViewModel: PlantViewModel by activityViewModels()
    private lateinit var plantViewModel: PlantViewModel

    //    private val plantViewModel: PlantViewModel by viewModels(
//        factoryProducer = { SavedStateViewModelFactory(application, this) }
//    )
    private lateinit var binding: ActivityPlantDetailsBinding
    private val plant: Plant = Plant(
        "TestPlant",
        "Test",
        R.drawable.ic_image_black_24dp.toString(),
        "2",
        "2020-07-09",
        "3",
        "2020-07-09"
    )
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    // TODO: 9-7-2020 set plant name as page title maybe..
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plant_details)
        binding.plant = plant

        bottomSheetBehavior = BottomSheetBehavior.from(constraint_bottom_sheet)
        setBottomSheet()

        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

//        binding.apply {
//            textPlantSpecies.text = plant?.species
//            imagePlant.setImageURI(Uri.parse(plant?.image))
//            textDaysToNextWater.text = plant?.waterInDays
//            textDaysToNextMist.text = plant?.mistInDays
//            textWaterEveryDays.text = plant?.waterEveryDays
//            textMistEveryDays.text = plant?.mistEveryDays
//
////            textDaysToNextWater.text = plant?.waterInDays?.toInt()?.let {
////                resources
////                    .getQuantityString(
////                        R.plurals.detail_water_in_days,
////                        it,
////                        plant?.waterInDays
////                    )
////            }
////            textDaysToNextMist.text = plant?.mistInDays?.toInt()?.let {
////                resources
////                    .getQuantityString(
////                        R.plurals.detail_mist_in_days,
////                        it,
////                        plant?.mistInDays
////                    )
////            }
////            textWaterEveryDays.text = plant?.waterEveryDays?.toInt()?.let {
////                resources
////                    .getQuantityString(
////                        R.plurals.detail_water_repeat,
////                        it,
////                        plant?.waterEveryDays
////                    )
////            }
////            textMistEveryDays.text = plant?.mistEveryDays?.toInt()?.let {
////                resources
////                    .getQuantityString(
////                        R.plurals.detail_mist_repeat,
////                        it,
////                        plant?.mistEveryDays
////                    )
////            }
//        }

//        datePlantNeedsWater = plant.waterDate
//        datePlantNeedsMist = plant.mistDate

//        plantViewModel.selectedPlant.observe(this, Observer<Plant> { plant ->
//            // TODO: 27-6-2020 update the UI
//            //            text_plant_name.text = plant.name
//
//            binding.apply {
//                textPlantSpecies.text = plant.species
//                imagePlant.setImageURI(Uri.parse(plant.image))
//                textDaysToNextWater.text = resources
//                    .getQuantityString(
//                        R.plurals.detail_water_in_days,
//                        plant.waterInDays,
//                        plant.waterInDays
//                    )
//                textDaysToNextMist.text = resources
//                    .getQuantityString(
//                        R.plurals.detail_mist_in_days,
//                        plant.mistInDays,
//                        plant.mistInDays
//                    )
//                textWaterEveryDays.text = resources
//                    .getQuantityString(
//                        R.plurals.detail_water_repeat,
//                        plant.waterEveryDays,
//                        plant.waterEveryDays
//                    )
//                textMistEveryDays.text = resources
//                    .getQuantityString(
//                        R.plurals.detail_mist_repeat,
//                        plant.mistEveryDays,
//                        plant.mistEveryDays
//                    )
//            }
//
//            datePlantNeedsWater = plant.waterDate
//            datePlantNeedsMist = plant.mistDate
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
//        })

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
                val editPlantIntent = Intent(this, XAddEditPlantActivity::class.java)
                Log.d(classNameTag, "Going to XAddEditPlantActivity. Plant ID is: ${plant.id}")
                editPlantIntent.putExtra("PLANT_ID", plant.id)
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