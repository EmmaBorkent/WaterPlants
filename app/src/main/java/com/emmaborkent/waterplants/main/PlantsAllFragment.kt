package com.emmaborkent.waterplants.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.emmaborkent.waterplants.NavigationDirections
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentPlantsAllBinding
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.plantdetails.PlantDetailsActivity

class PlantsAllFragment : Fragment() {

    private lateinit var binding: FragmentPlantsAllBinding
    private val plantViewModel: PlantViewModel by activityViewModels()
    lateinit var adapter: PlantsAllAdapter
    lateinit var layoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plants_all, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = PlantsAllAdapter {
//            Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show()
//            plantViewModel.select(plant)
//            goToPlantDetails()
            // TODO: 15-7-2020 Change Plant ID to real Plant ID
            view?.findNavController()
                ?.navigate(NavigationDirections.actionGlobalDetailsFragment(2))
        }

        layoutManager = GridLayoutManager(context, 2)

        binding.apply {
            recyclerViewAllPlants.adapter = adapter
            recyclerViewAllPlants.layoutManager = layoutManager
        }

        plantViewModel.allPlants.observe(viewLifecycleOwner, Observer { plants ->
            plants?.let { adapter.setPlants(it) }
        })
    }

    private fun goToPlantDetails() {
        val plantDetailsIntent = Intent(activity, PlantDetailsActivity::class.java)
        startActivity(plantDetailsIntent)
    }
}