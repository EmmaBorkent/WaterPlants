package com.emmaborkent.waterplants.allplants

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.emmaborkent.waterplants.NavigationDirections
import com.emmaborkent.waterplants.PlantViewModel
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentPlantsAllBinding
import com.emmaborkent.waterplants.main.TabbedFragmentDirections
import com.emmaborkent.waterplants.util.PlantClickListener
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("onViewCreated called")

        adapter = PlantsAllAdapter()
        adapter.setOnItemClickListener(object : PlantClickListener {
            override fun onItemClick(plantId: Int) {
                Timber.i("Plant ID: $plantId")
                view.findNavController().navigate(NavigationDirections.actionGlobalDetailsFragment(plantId))
            }
        })

        layoutManager = GridLayoutManager(context, 2)

        binding.apply {
            recyclerViewAllPlants.adapter = adapter
            recyclerViewAllPlants.layoutManager = layoutManager
            floatingActionButton.setOnClickListener {
                view.findNavController().navigate(TabbedFragmentDirections.actionTabbedFragmentToAddEditPlantFragment())
            }
        }

        viewModel.allPlants.observe(viewLifecycleOwner, { plants ->
            plants?.let {
                adapter.setPlants(it)
                if (plants.isNotEmpty()) {
                    binding.textNoPlants.visibility = View.GONE
                }
            }
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