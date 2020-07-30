package com.emmaborkent.waterplants.allplants

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.emmaborkent.waterplants.NavigationDirections
import com.emmaborkent.waterplants.PlantViewModel
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentPlantsAllBinding
import timber.log.Timber

class PlantsAllFragment : Fragment() {

    private lateinit var binding: FragmentPlantsAllBinding
    private val viewModel by activityViewModels<PlantViewModel>()
    lateinit var adapter: PlantsAllAdapter
    lateinit var layoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView called")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plants_all, container, false)
//        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)
//        Timber.i("ViewModelProviders called")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.i("onActivityCreated called")

        adapter = PlantsAllAdapter {
            // TODO: 15-7-2020 Change Plant ID to real Plant ID
            view?.findNavController()
                ?.navigate(NavigationDirections.actionGlobalDetailsFragment(2))
        }

        layoutManager = GridLayoutManager(context, 2)

        binding.apply {
            recyclerViewAllPlants.adapter = adapter
            recyclerViewAllPlants.layoutManager = layoutManager
        }

        viewModel.allPlants.observe(viewLifecycleOwner, Observer { plants ->
            plants?.let { adapter.setPlants(it) }
        })
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