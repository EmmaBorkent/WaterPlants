package com.emmaborkent.waterplants.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.model.PLANT_ID
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.viewmodel.PlantViewModel
import kotlinx.android.synthetic.main.fragment_plants_all.*

class PlantsAllFragment : Fragment() {
    private lateinit var plantViewModel: PlantViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plants_all, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        val adapter =
            PlantsAllAdapter { plant ->
                goToPlantDetails(plant)
            }
        recycler_view_all_plants.adapter = adapter
        recycler_view_all_plants.layoutManager = GridLayoutManager(context, 2)
        recycler_view_all_plants.setHasFixedSize(true)

        // TODO: 25-6-2020 Remove commented out code
//        plantViewModel = ViewModelProvider(
//            requireActivity(),
//            defaultViewModelProviderFactory
//        ).get(PlantsViewModel::class.java)

//        plantViewModel = ViewModelProvider(requireActivity()).get(PlantsViewModel::class.java)
        plantViewModel.allPlants.observe(viewLifecycleOwner, Observer { plants ->
            plants?.let { adapter.setPlants(it) }
        })
    }

    private fun goToPlantDetails(plant: Plant) {
        val plantDetailsIntent = Intent(activity, PlantDetailsActivity::class.java)
        plantDetailsIntent.putExtra(PLANT_ID, plant.id)
        startActivity(plantDetailsIntent)
    }
}