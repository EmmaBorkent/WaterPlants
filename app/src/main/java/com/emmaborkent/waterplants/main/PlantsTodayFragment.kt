package com.emmaborkent.waterplants.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentPlantsTodayBinding
import com.emmaborkent.waterplants.util.PlantsTodayListener

class PlantsTodayFragment : Fragment() {

    private lateinit var binding: FragmentPlantsTodayBinding
    private val plantViewModel: PlantViewModel by activityViewModels()
    //    private lateinit var plantViewModel: PlantViewModel
    private lateinit var waterPlantsLayoutManager: LinearLayoutManager
    private lateinit var waterPlantsAdapter: PlantsTodayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentPlantsTodayBinding>(
            inflater,
            R.layout.fragment_plants_today,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        waterPlantsAdapter = PlantsTodayAdapter(
            plantViewModel,
            PlantsTodayListener {
                Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show()
            })

        waterPlantsLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )

        binding.apply {
            recyclerViewWaterPlants.adapter = waterPlantsAdapter
            recyclerViewWaterPlants.layoutManager = waterPlantsLayoutManager
        }

        plantViewModel.plantsThatNeedWater.observe(viewLifecycleOwner, Observer { waterPlants ->
            waterPlants?.let { waterPlantsAdapter.setWaterPlants(it) }
        })
        plantViewModel.plantsThatNeedMist.observe(viewLifecycleOwner, Observer { mistPlants ->
            mistPlants?.let { waterPlantsAdapter.setMistPlants(it) }
        })

//        setTextHowManyPlantsNeedAction()
    }

//    private fun goToPlantDetails() {
//        val plantDetailsIntent = Intent(activity, PlantDetailsActivity::class.java)
//        startActivity(plantDetailsIntent)
//    }

    // TODO: 25-6-2020 Create function setTextHowManyPlantsNeedAction()
//    private fun setTextHowManyPlantsNeedAction() {
//        val textHowManyActions = viewModel.allPlantsThatNeedWaterOrMist.size
//        if (textHowManyActions != 0) {
//            text_subtitle.text = resources
//                .getQuantityString(R.plurals.main_subtitle, textHowManyActions, textHowManyActions)
//        } else {
//            text_subtitle.text = resources.getString(R.string.main_subtitle_zero)
//        }
//    }

}
