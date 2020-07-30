package com.emmaborkent.waterplants.plantdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.emmaborkent.waterplants.PlantViewModel
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentDetailsBinding
import timber.log.Timber

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel by activityViewModels<PlantViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView called")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container,
            false)
        Timber.i("ViewModelProviders called")

//        binding.plant = plantViewModel.testPlant
        binding.collapsingToolbar.title = viewModel.testPlant.species
        // TODO: 24-7-2020 change activity title, this does not work, also in addEditPlantFragment
        activity?.title = viewModel.testPlant.name
        // TODO: 16-7-2020 Change plantId to real plant ID
        binding.buttonFabEdit.setOnClickListener {
            view?.findNavController()?.navigate(DetailsFragmentDirections
                .actionDetailsFragmentToAddEditPlantFragment(1))
        }
        val args = DetailsFragmentArgs.fromBundle(requireArguments())

        Timber.i("Plant ID: ${args.plantId}")
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach called")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
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