package com.emmaborkent.waterplants.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.Plant
import com.emmaborkent.waterplants.database.PlantDatabaseHandler
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var allPlantsLayoutManager: LinearLayoutManager
    private lateinit var allPlantsAdapter: AllPlantsRecyclerAdapter
    private lateinit var dbHandler: PlantDatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))

        bottomSheetBehavior = BottomSheetBehavior.from(main_constraint_layout_bottom)
        bottomSheet()

        add_new_plant.setOnClickListener {
            val newPlantIntent = Intent(this, AddEditPlantActivity::class.java)
            startActivity(newPlantIntent)
        }

        showAllPlants()

//        PlantDatabaseHandler(this).printAllPlantIds()
//        PlantDatabaseHandler(this).deleteAllPlants()

        // TODO: 10-4-2020 Create function to check amount of plants that need water, can only be done after dates are added to plants
        // TODO: 10-4-2020 Create function that sets the plural of how many plants to water 

    }

    private fun bottomSheet() {

        // Get display height to calculate peekHeight for the Bottom Sheet
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        // This might go wrong with screens with different pixel densities
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

    private fun showAllPlants() {
        dbHandler = PlantDatabaseHandler(this)
        val allPlants: ArrayList<Plant> = dbHandler.readAllPlants()
        allPlants.reverse()

        allPlantsLayoutManager = GridLayoutManager(this, 2)
        main_recycler_view_all_plants.layoutManager = allPlantsLayoutManager

        allPlantsAdapter = AllPlantsRecyclerAdapter(allPlants, this) { plant ->
            val plantDetailsIntent = Intent(this, PlantDetailsActivity::class.java)
            Log.d("INTENT", "Going to PlantDetailsActivity. Plant ID is: ${plant.id}")
            plantDetailsIntent.putExtra("PLANT_ID", plant.id)
            startActivity(plantDetailsIntent)
        }
        main_recycler_view_all_plants.adapter = allPlantsAdapter
    }
}
