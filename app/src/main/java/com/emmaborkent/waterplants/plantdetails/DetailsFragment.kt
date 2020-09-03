package com.emmaborkent.waterplants.plantdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentDetailsBinding
import kotlinx.android.synthetic.main.fragment_tabbed.view.*
import timber.log.Timber


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModelFactory: DetailsViewModelFactory
    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView called")
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_details, container,
            false
        )
        val plantId = DetailsFragmentArgs.fromBundle(requireArguments()).plantId
        val application = requireNotNull(this.activity).application
        viewModelFactory = DetailsViewModelFactory(plantId, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.detailsViewModel = viewModel

        viewModel.plant.observe(viewLifecycleOwner, Observer { plant ->
            setActivityTitle(plant.name)
            binding.collapsingToolbar.title = plant.species
        })

//        setActivityTitle(viewModel.plant.value?.name)
//        binding.collapsingToolbar.title = viewModel.plant.value?.species

        binding.buttonFabEdit.setOnClickListener {
            view?.findNavController()?.navigate(
                DetailsFragmentDirections
                    .actionDetailsFragmentToAddEditPlantFragment(plantId)
            )
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