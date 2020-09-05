package com.emmaborkent.waterplants.plantstoday

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmaborkent.waterplants.NavigationDirections
import com.emmaborkent.waterplants.PlantViewModel
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentPlantsTodayBinding
import com.emmaborkent.waterplants.util.PlantClickListener
import timber.log.Timber

class PlantsTodayFragment : Fragment() {

    private lateinit var binding: FragmentPlantsTodayBinding
    private val viewModel by activityViewModels<PlantViewModel>()
    private lateinit var waterPlantsLayoutManager: LinearLayoutManager
    private lateinit var waterPlantsAdapter: PlantsTodayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView called")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plants_today, container,
            false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.i("onActivityCreated called")

        waterPlantsAdapter = PlantsTodayAdapter(viewModel)
        waterPlantsAdapter.setOnItemClickListener(object : PlantClickListener {
            override fun onItemClick(plantId: Int) {
                view?.findNavController()?.navigate(NavigationDirections.actionGlobalDetailsFragment(plantId))
            }
        })

        waterPlantsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
            false)

        binding.apply {
            recyclerViewWaterPlants.adapter = waterPlantsAdapter
            recyclerViewWaterPlants.layoutManager = waterPlantsLayoutManager
        }

        viewModel.plantsThatNeedWater.observe(viewLifecycleOwner, Observer { waterPlants ->
            waterPlants?.let { waterPlantsAdapter.setWaterPlants(it) }
        })
        viewModel.plantsThatNeedMist.observe(viewLifecycleOwner, Observer { mistPlants ->
            mistPlants?.let { waterPlantsAdapter.setMistPlants(it) }
        })
        viewModel.countAllPlantsThatNeedWaterOrMist.observe(viewLifecycleOwner, Observer { count ->
            count?.let {
                binding.textSubtitle.text = if (count != 0) {
                    resources.getQuantityString(R.plurals.main_subtitle, it, it)
                } else {
                    resources.getString(R.string.main_subtitle_zero)
                }
            }
        })
    }


    
    // Lifecycle Logging

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
