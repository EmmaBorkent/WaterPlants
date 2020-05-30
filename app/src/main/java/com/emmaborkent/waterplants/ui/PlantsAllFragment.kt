package com.emmaborkent.waterplants.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.Plant
import com.emmaborkent.waterplants.database.PlantDatabaseHandler
import kotlinx.android.synthetic.main.fragment_plants_all.*

class PlantsAllFragment : Fragment() {
    private lateinit var dbHandler: PlantDatabaseHandler
    private lateinit var allPlantsLayoutManager: LinearLayoutManager
    private lateinit var allPlantsAdapter: AllPlantsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plants_all, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dbHandler = PlantDatabaseHandler.getInstance(context!!)
        showAllPlantsInRecyclerView()
    }

    private fun showAllPlantsInRecyclerView() {
        val allPlants = getAllPlantsFromDatabase()
        allPlants.reverse()

        main_recycler_view_all_plants.setItemViewCacheSize(10)

        allPlantsLayoutManager = GridLayoutManager(context, 2)
        main_recycler_view_all_plants.layoutManager = allPlantsLayoutManager
        allPlantsAdapter = AllPlantsRecyclerAdapter(allPlants, context!!) { plant ->
            goToPlantDetails(plant)
        }
        main_recycler_view_all_plants.adapter = allPlantsAdapter
    }

    private fun getAllPlantsFromDatabase(): ArrayList<Plant> {
        return dbHandler.readAllPlants()
    }

    private fun goToPlantDetails(plant: Plant) {
        val plantDetailsIntent = Intent(activity, PlantDetailsActivity::class.java)
        plantDetailsIntent.putExtra("PLANT_ID", plant.id)
        startActivity(plantDetailsIntent)
    }
}
