package com.emmaborkent.waterplants.plantdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentDetailsBinding
import com.emmaborkent.waterplants.databinding.FragmentPlantDetailsBinding
import com.emmaborkent.waterplants.main.PlantViewModel
import com.emmaborkent.waterplants.model.Plant
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DetailsFragment : Fragment() {

//    private val classNameTag: String = PlantDetailsActivity::class.java.simpleName
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var plantViewModel: PlantViewModel

    private val plant: Plant = Plant(
        "TestPlant",
        "Test",
        R.drawable.ic_image_black_24dp.toString(),
        "2",
        "2020-07-09",
        "3",
        "2020-07-09"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        binding.plant = plant
        binding.collapsingToolbar.title = plant.species
        activity?.title = plant.name
        // TODO: 16-7-2020 Change plantId to real plant ID
        binding.buttonFabEdit.setOnClickListener {
            view?.findNavController()?.navigate(DetailsFragmentDirections.actionDetailsFragmentToAddEditPlantFragment(1))
        }
        val args = DetailsFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "PlantID: ${args.plantId}", Toast.LENGTH_SHORT).show()
        return binding.root
    }
}