package com.emmaborkent.waterplants.plantdetails

//import android.os.Bundle
//import android.Util.DisplayMetrics
//import android.Util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.ViewModelProvider
//import com.emmaborkent.waterplants.R
//import com.emmaborkent.waterplants.databinding.FragmentPlantDetailsBinding
//import com.emmaborkent.waterplants.PlantViewModel
//import com.emmaborkent.waterplants.model.Plant
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import kotlinx.android.synthetic.main.activity_plant_details.*
//
//class XPlantDetailsFragment : Fragment() {
//
//    private val classNameTag: String = XPlantDetailsActivity::class.java.simpleName
//    private lateinit var binding: FragmentPlantDetailsBinding
//    private lateinit var plantViewModel: PlantViewModel
//    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
//
//    private val plant: Plant = Plant(
//        "TestPlant",
//        "Test",
//        R.drawable.ic_image_black_24dp.toString(),
//        1,
//        "2020-07-09",
//        3,
//        "2020-07-09"
//    )
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plant_details, container, false)
//        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)
//        bottomSheetBehavior = BottomSheetBehavior.from(constraint_bottom_sheet)
//        setBottomSheet()
//        binding.plant = plant
//        return binding.root
//    }
//
//    private fun setBottomSheet() {
//        // Get display height to calculate peekHeight for the Bottom Sheet
//        val displayMetrics = DisplayMetrics()
//        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
//        // TODO: Setting the screen height this way might go wrong with screens with different pixel densities, think of a fix
//        val halfScreenHeight = displayMetrics.heightPixels * 0.36
//        bottomSheetBehavior.peekHeight = halfScreenHeight.toInt()
//
//        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior
//        .BottomSheetCallback() {
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_EXPANDED -> Log.i(classNameTag, "Expanded State")
//                    BottomSheetBehavior.STATE_COLLAPSED -> Log.i(classNameTag, "Collapsed State")
//                    BottomSheetBehavior.STATE_DRAGGING -> Log.i(classNameTag, "Dragging...")
//                    BottomSheetBehavior.STATE_SETTLING -> Log.i(classNameTag, "Settling...")
//                    BottomSheetBehavior.STATE_HALF_EXPANDED -> Log.i(
//                        classNameTag,
//                        "Half Expended State"
//                    )
//                    BottomSheetBehavior.STATE_HIDDEN -> Log.i(classNameTag, "Hidden State")
//                }
//            }
//        })
//    }
//}