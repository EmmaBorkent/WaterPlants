package com.emmaborkent.waterplants.plantdetails

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.emmaborkent.waterplants.PlantViewModel
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentPlantDetailsBinding
import timber.log.Timber

class PlantDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPlantDetailsBinding
    private val plantViewModel by activityViewModels<PlantViewModel>()
    private var plantId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
        plantId = PlantDetailsFragmentArgs.fromBundle(requireArguments()).plantId
        plantViewModel.initializePlant(plantId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView called")
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_plant_details, container,
            false
        )

        binding.lifecycleOwner = this
        binding.plantViewModel = plantViewModel

        plantViewModel.plant.observe(viewLifecycleOwner, Observer { plant ->
            if (plant.species != "") {
                setActivityTitle(plant.species)
            } else {
                setActivityTitle(plant.name)
            }

            val imageUri: Uri = Uri.parse(plant.image)
            binding.imagePlant.setImageURI(imageUri)

            if (plant.waterInDays > 0) {
                binding.textDaysToNextWater.text = resources.getQuantityString(R.plurals.detail_water_in_days, plant.waterInDays, plant.waterInDays)
                binding.buttonGiveWater.isClickable = false
            } else {
                binding.textDaysToNextWater.text = resources.getString(R.string.detail_in_zero_days)
                binding.buttonGiveWater.isClickable = true
            }

            if (plant.mistInDays > 0) {
                binding.textDaysToNextMist.text = resources.getQuantityString(R.plurals.detail_water_in_days, plant.waterInDays, plant.waterInDays)
                binding.buttonGiveMist.isClickable = false
            } else {
                binding.textDaysToNextMist.text = resources.getString(R.string.detail_in_zero_days)
                binding.buttonGiveMist.isClickable = true
            }
        })

        binding.buttonFabEdit.setOnClickListener {
            view?.findNavController()?.navigate(
                PlantDetailsFragmentDirections
                    .actionDetailsFragmentToAddEditPlantFragment(plantId)
            )
        }

        binding.buttonGiveWater.setOnClickListener {
            if (binding.buttonGiveWater.isChecked) {
                plantViewModel.giveWater(plantViewModel.plant.value!!)
            } else {
                plantViewModel.undoWaterGift(plantViewModel.plant.value!!)
            }
        }

        binding.buttonGiveMist.setOnClickListener {
            if (binding.buttonGiveMist.isChecked) {
                plantViewModel.giveMist(plantViewModel.plant.value!!)
            } else {
                plantViewModel.undoMistGift(plantViewModel.plant.value!!)
            }
        }

        return binding.root
    }

    private fun Fragment.setActivityTitle(string: String?) {
        (activity as AppCompatActivity?)!!.supportActionBar?.title = string
    }

    // Lifecycle Tracking

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach called")
    }
    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }
    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }
    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }
    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView called")
    }
    override fun onDetach() {
        super.onDetach()
        Timber.i("onDetach called")
    }
}