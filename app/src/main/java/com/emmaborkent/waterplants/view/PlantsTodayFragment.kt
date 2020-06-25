package com.emmaborkent.waterplants.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.model.PLANT_ID
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.viewmodel.PlantViewModel
import kotlinx.android.synthetic.main.fragment_plants_today.*

class PlantsTodayFragment : Fragment() {
    private lateinit var plantViewModel: PlantViewModel
    private lateinit var waterPlantsLayoutManager: LinearLayoutManager
    private lateinit var waterPlantsAdapter: PlantsTodayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plants_today, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        waterPlantsAdapter = PlantsTodayAdapter(plantViewModel) { plant ->
            goToPlantDetails(plant)
        }
        recycler_view_water_plants.adapter = waterPlantsAdapter
        waterPlantsLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        recycler_view_water_plants.setHasFixedSize(true)
        recycler_view_water_plants.layoutManager = waterPlantsLayoutManager

        plantViewModel.plantsThatNeedWater.observe(viewLifecycleOwner, Observer { waterPlants ->
            waterPlants?.let { waterPlantsAdapter.setWaterPlants(it) }
        })
        plantViewModel.plantsThatNeedMist.observe(viewLifecycleOwner, Observer { mistPlants ->
            mistPlants?.let { waterPlantsAdapter.setMistPlants(it) }
        })

//        setTextHowManyPlantsNeedAction()
    }

    private fun goToPlantDetails(plant: Plant) {
        val plantDetailsIntent = Intent(activity, PlantDetailsActivity::class.java)
        plantDetailsIntent.putExtra(PLANT_ID, plant.id)
        startActivity(plantDetailsIntent)
    }

    // TODO: 25-6-2020 Create function setTextHowManyPlantsNeedAction()
//    private fun setTextHowManyPlantsNeedAction() {
//        val textHowManyActions = viewModel.allPlantsThatNeedWaterOrMist.size
//        if (textHowManyActions != 0) {
//            text_subtitle.text = resources
//                .getQuantityString(R.plurals.main_subtitle, textHowManyActions, textHowManyActions)
//        } else {
//            text_subtitle.text = resources.getString(R.string.main_subtitle_zero)
//        }
//    }

}
