package com.emmaborkent.waterplants.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.ParseFormatDates
import com.emmaborkent.waterplants.database.Plant
import com.emmaborkent.waterplants.database.PlantDatabaseHandler
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val classNameTag: String = MainActivity::class.java.simpleName
    private lateinit var dbHandler: PlantDatabaseHandler
    private lateinit var allPlantsLayoutManager: LinearLayoutManager
    private lateinit var allPlantsAdapter: AllPlantsRecyclerAdapter
    private lateinit var waterPlantsLayoutManager: LinearLayoutManager
    private lateinit var waterPlantsAdapter: WaterPlantsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        bottomSheetBehavior = BottomSheetBehavior.from(main_constraint_layout_bottom)
        setBottomSheet()
        add_new_plant.setOnClickListener {
            val newPlantIntent = Intent(this, AddEditPlantActivity::class.java)
            startActivity(newPlantIntent)
        }
//        Log.d(classNameTag, "Print all plant actions:")
//        PlantDatabaseHandler(this).printAllPlantsThatNeedWater()
        showAllPlantsInRecyclerView()
        // TODO: 10-4-2020 Create function to check amount of plants that need water, can only be
        //  done after dates are added to plants
        showPlantsThatNeedWaterToday()
        // TODO: 10-4-2020 Create function that sets the plural of how many plants to water
    }

    private fun setBottomSheet() {
        calculatePeekHeightForBottomSheet()
        setBottomSheetCallback()
    }

    private fun calculatePeekHeightForBottomSheet() {
        // TODO: 17-4-2020 Check if this goes wrong with screens with different pixel densities
        // Get display height to calculate peekHeight for the Bottom Sheet
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        // This might go wrong with screens with different pixel densities
        val halfScreenHeight = displayMetrics.heightPixels * 0.41
        bottomSheetBehavior.peekHeight = halfScreenHeight.toInt()
    }

    private fun setBottomSheetCallback() {
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior
        .BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })
    }

    private fun showAllPlantsInRecyclerView() {
        val allPlants = getAllPlantsFromDatabase()
        allPlants.reverse()
        allPlantsLayoutManager = GridLayoutManager(this, 2)
        main_recycler_view_all_plants.layoutManager = allPlantsLayoutManager
        allPlantsAdapter = AllPlantsRecyclerAdapter(allPlants, this) { plant ->
            val plantDetailsIntent = Intent(this, PlantDetailsActivity::class.java)
            Log.d(classNameTag, "Going to PlantDetailsActivity. Plant ID is: ${plant.id}")
            plantDetailsIntent.putExtra("PLANT_ID", plant.id)
            startActivity(plantDetailsIntent)
        }
        main_recycler_view_all_plants.adapter = allPlantsAdapter
    }

    private fun getAllPlantsFromDatabase(): ArrayList<Plant> {
        dbHandler = PlantDatabaseHandler(this)
        return dbHandler.readAllPlants()
    }

    private fun showPlantsThatNeedWaterToday() {
        val plantsThatNeedWater = getPlantsThatNeedWater()
        val plantsThatNeedMist = getPlantsThatNeedMist()

        for (plant in plantsThatNeedWater) {
            plant.needsWater = true
            plant.needsMist = false
        }

        for (plant in plantsThatNeedMist) {
            plant.needsWater = false
            plant.needsMist = true
        }

        val allPlantsThatNeedWaterOrMist = ArrayList<Plant>()
        allPlantsThatNeedWaterOrMist.addAll(plantsThatNeedWater)
        allPlantsThatNeedWaterOrMist.addAll(plantsThatNeedMist)

        waterPlantsLayoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)
        main_recycler_view_water_plants.layoutManager = waterPlantsLayoutManager
        waterPlantsAdapter = WaterPlantsRecyclerAdapter(allPlantsThatNeedWaterOrMist, this) { plant ->
//            val plantDetailsIntent = Intent(this, PlantDetailsActivity::class.java)
//            Log.d(classNameTag, "Going to PlantDetailsActivity. Plant ID is: ${plant.id}")
//            plantDetailsIntent.putExtra("PLANT_ID", plant.id)
//            startActivity(plantDetailsIntent)
            // TODO: 24-4-2020 Functionality on clicking plant in Plants that Need water Recycler
            //  view
            Toast.makeText(this, "Watered ${plant.name} today!", Toast.LENGTH_SHORT).show()
        }
        main_recycler_view_water_plants.adapter = waterPlantsAdapter
    }

    private fun getPlantsThatNeedWater(): ArrayList<Plant> {
        // TODO: 24-4-2020 make one function of duplicate code
        val getCurrentDate = LocalDate.now()
        val dateString = ParseFormatDates().dateToStringDefault(getCurrentDate)
        return dbHandler.getPlantsThatNeedWater(dateString)
    }

    private fun getPlantsThatNeedMist(): ArrayList<Plant> {
        val currentDate = LocalDate.now()
        val dateString = ParseFormatDates().dateToStringDefault(currentDate)
        return dbHandler.getPlantsThatNeedMist(dateString)
    }
}
