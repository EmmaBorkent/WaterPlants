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
import com.emmaborkent.waterplants.model.PlantEntity
import com.emmaborkent.waterplants.viewmodel.PlantsAllViewModel
import kotlinx.android.synthetic.main.fragment_plants_all.*

class PlantsAllFragment : Fragment() {
    private lateinit var plantViewModel: PlantsAllViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plants_all, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter =
            PlantsAllAdapter { plant ->
                goToPlantDetails(plant)
            }
        recycler_view_all_plants.adapter = adapter
        recycler_view_all_plants.layoutManager = GridLayoutManager(context, 2)
        recycler_view_all_plants.setHasFixedSize(true)

        plantViewModel = ViewModelProvider(this).get(PlantsAllViewModel::class.java)

//        plantViewModel = ViewModelProvider(
//            requireActivity(),
//            defaultViewModelProviderFactory
//        ).get(PlantsAllViewModel::class.java)

//        plantViewModel = ViewModelProvider(requireActivity()).get(PlantsAllViewModel::class.java)
        plantViewModel.allPlants.observe(viewLifecycleOwner, Observer { plants ->
            plants?.let { adapter.setPlants(it) }
        })
    }

    private fun goToPlantDetails(plant: PlantEntity) {
        val plantDetailsIntent = Intent(activity, PlantDetailsActivity::class.java)
        plantDetailsIntent.putExtra(PLANT_ID, plant.id)
        startActivity(plantDetailsIntent)
    }
}