package com.emmaborkent.waterplants.ui

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.ParseFormatDates
import com.emmaborkent.waterplants.database.Plant
import com.emmaborkent.waterplants.database.PlantDatabaseHandler
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_plant_details.*

class MainActivity : AppCompatActivity() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val classNameTag: String = MainActivity::class.java.simpleName
    private lateinit var dbHandler: PlantDatabaseHandler
    private lateinit var allPlantsLayoutManager: LinearLayoutManager
    private lateinit var allPlantsAdapter: AllPlantsRecyclerAdapter
    private lateinit var waterPlantsLayoutManager: LinearLayoutManager
    private lateinit var waterPlantsAdapter: WaterPlantsRecyclerAdapter
    private var plantsThatNeedWater = ArrayList<Plant>()
    private var plantsThatNeedMist = ArrayList<Plant>()

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
        showAllPlantsInRecyclerView()
        showPlantsThatNeedWaterToday()
        setTextHowManyPlantsNeedAction()
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
        plantsThatNeedWater = getPlantsThatNeedWater()
        plantsThatNeedMist = getPlantsThatNeedMist()

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

        waterPlantsLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        main_recycler_view_water_plants.layoutManager = waterPlantsLayoutManager
        waterPlantsAdapter =
            WaterPlantsRecyclerAdapter(allPlantsThatNeedWaterOrMist, this) { plant ->
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
        return dbHandler.getPlantsThatNeedWater(ParseFormatDates().getDefaultDateString())
    }

    private fun getPlantsThatNeedMist(): ArrayList<Plant> {
        return dbHandler.getPlantsThatNeedMist(ParseFormatDates().getDefaultDateString())
    }

    private fun setTextHowManyPlantsNeedAction() {
        val textHowManyActions = plantsThatNeedMist.size + plantsThatNeedWater.size
        if (textHowManyActions != 0) {
            main_subtitle.text = resources
                .getQuantityString(R.plurals.main_subtitle, textHowManyActions, textHowManyActions)
        } else {
            main_subtitle.text = resources.getString(R.string.main_subtitle_zero)
        }
    }
}
