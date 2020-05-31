package com.emmaborkent.waterplants.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.ParseFormatDates
import com.emmaborkent.waterplants.database.Plant
import com.emmaborkent.waterplants.database.PlantDatabaseHandler
import kotlinx.android.synthetic.main.fragment_plants_today.*

class PlantsTodayFragment : Fragment() {
    private lateinit var dbHandler: PlantDatabaseHandler
    private lateinit var waterPlantsLayoutManager: LinearLayoutManager
    private lateinit var waterPlantsAdapter: WaterPlantsRecyclerAdapter
    private var plantsThatNeedWater = ArrayList<Plant>()
    private var plantsThatNeedMist = ArrayList<Plant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plants_today, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dbHandler = PlantDatabaseHandler.getInstance(context!!.applicationContext)

        setWaterAndMistArrayLists()
        showPlantsThatNeedWaterToday()
        setTextHowManyPlantsNeedAction()
    }

    private fun setWaterAndMistArrayLists() {
        plantsThatNeedWater = getPlantsThatNeedWater()
        plantsThatNeedMist = getPlantsThatNeedMist()
    }

    private fun getPlantsThatNeedWater(): ArrayList<Plant> {
        return dbHandler.getPlantsThatNeedWater(ParseFormatDates().getDefaultDateAsString())
    }

    private fun getPlantsThatNeedMist(): ArrayList<Plant> {
        return dbHandler.getPlantsThatNeedMist(ParseFormatDates().getDefaultDateAsString())
    }

    // TODO: 8-5-2020 shorten function
    private fun showPlantsThatNeedWaterToday() {
        setWaterNeed()
        setMistNeed()

        main_recycler_view_water_plants.setHasFixedSize(true)
        main_recycler_view_water_plants.setItemViewCacheSize(10)

        val allPlantsThatNeedWaterOrMist = ArrayList<Plant>()
        allPlantsThatNeedWaterOrMist.addAll(plantsThatNeedWater)
        allPlantsThatNeedWaterOrMist.addAll(plantsThatNeedMist)

        waterPlantsLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        main_recycler_view_water_plants.layoutManager = waterPlantsLayoutManager
        waterPlantsAdapter =
            WaterPlantsRecyclerAdapter(allPlantsThatNeedWaterOrMist, context!!.applicationContext) { plant ->
                goToPlantDetails(plant)
            }
        main_recycler_view_water_plants.adapter = waterPlantsAdapter
    }

    private fun goToPlantDetails(plant: Plant) {
        val plantDetailsIntent = Intent(activity, PlantDetailsActivity::class.java)
        plantDetailsIntent.putExtra("PLANT_ID", plant.id)
        startActivity(plantDetailsIntent)
    }

    private fun setWaterNeed() {
        for (plant in plantsThatNeedWater) {
            plant.needsWater = true
            plant.needsMist = false
        }
    }

    private fun setMistNeed() {
        for (plant in plantsThatNeedMist) {
            plant.needsWater = false
            plant.needsMist = true
        }
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
