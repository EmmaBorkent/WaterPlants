package com.emmaborkent.waterplants.addeditplant

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentAddEditPlantBinding
import com.emmaborkent.waterplants.PlantViewModel
import kotlinx.android.synthetic.main.activity_add_edit_plant.*
import timber.log.Timber

class AddEditPlantFragment : Fragment() {

    private lateinit var binding: FragmentAddEditPlantBinding
    private lateinit var plantViewModel: PlantViewModel
    private lateinit var clickedButtonView: Button

//    private var imageIsChanged = false

//    companion object {
//        const val EXTRA_REPLY_NAME = "com.emmaborkent.waterplants.REPLY_NAME"
//        const val EXTRA_REPLY_SPECIES = "com.emmaborkent.waterplants.REPLY_SPECIES"
//        const val EXTRA_REPLY_IMAGE = "com.emmaborkent.waterplants.REPLY_IMAGE"
//        const val EXTRA_REPLY_WATER_DATE = "com.emmaborkent.waterplants.REPLY_WATER_DATE"
//        const val EXTRA_REPLY_WATER_EVERY_DAYS =
//            "com.emmaborkent.waterplants.REPLY_WATER_EVERY_DAYS"
//        const val EXTRA_REPLY_MIST_DATE = "com.emmaborkent.waterplants.REPLY_MIST_DATE"
//        const val EXTRA_REPLY_MIST_EVERY_DAYS = "com.emmaborkent.waterplants.REPLY_MIST_EVERY_DAYS"
//
//        const val PICK_IMAGE_CODE = 1000
//        const val PERMISSION_CODE = 1001
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView called")
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_add_edit_plant, container, false)
        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)
        Timber.i("ViewModelProviders called")

        setupPageContent()
        // TODO: 16-4-2020 Shouldn't I have used onClick method in XML? that way you always need
        //  to pass the view
        binding.buttonDatePlantsNeedsWater.setOnClickListener {
            showDatePickerDialog(button_date_plants_needs_water)
        }
        binding.buttonDatePlantsNeedsMist.setOnClickListener {
            showDatePickerDialog(button_date_plants_needs_mist)
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu_add_edit_plant, menu)
        if (isEditActivity()) {
            val saveNewPlantMenuItem = menu.findItem(R.id.action_save_new)
            saveNewPlantMenuItem.isVisible = false
            val updatePlantMenuItem = menu.findItem(R.id.action_save_update)
            updatePlantMenuItem.isVisible = true
            val deleteMenuItem: MenuItem = menu.findItem(R.id.action_delete)
            deleteMenuItem.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // TODO: 9-7-2020 There should be only one savePlant function used from the view model
            R.id.action_save_new -> {
                // save plant
                view?.findNavController()?.navigate(AddEditPlantFragmentDirections.actionAddEditPlantFragmentToTabbedFragment())
            }
            R.id.action_save_update -> {
                // update plant
                // TODO: 16-7-2020 Change to real Plant ID
                view?.findNavController()?.navigate(AddEditPlantFragmentDirections.actionAddEditPlantFragmentToDetailsFragment(1))
            }
            R.id.action_delete -> {
                // are you sure?
                // delete plant
                view?.findNavController()?.navigate(AddEditPlantFragmentDirections.actionAddEditPlantFragmentToTabbedFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupPageContent() {
        if (isEditActivity()) {
            setupPageToEditPlant()
        } else {
            setupPageToAddPlant()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun showDatePickerDialog(clickedButton: Button) {
        clickedButtonView = clickedButton
        val datePicker =
            DatePickerDialogFragment()
        // TODO: 24-7-2020 Call isEditActivity only once an set value to variable which you can use to determine if it isEditActivy
        if (isEditActivity()) {
            val args = Bundle()
            // TODO: 23-4-2020 Create key value pairs for Bundles and Intents
            //  see 2.2.13 String constants, naming, and values
            args.putString("DATE", clickedButton.text.toString())
            datePicker.arguments = args
        }
        datePicker.show(childFragmentManager, "datePicker")
    }

    // TODO: 12-7-2020 Change get intent to Define Destination Arguments
    // https://developer.android.com/guide/navigation/navigation-pass-data
    // TODO: 24-7-2020 Call isEditActivity only once an set value to variable which you can use to determine if it isEditActivity
    private fun isEditActivity(): Boolean {
        val args = AddEditPlantFragmentArgs.fromBundle(requireArguments())
        return args.plantId != 0
    }

    private fun setupPageToEditPlant() {
        activity?.title = getString(R.string.edit_plant_toolbar)
        binding.plant = plantViewModel.testPlant
    }

    private fun setupPageToAddPlant() {
        activity?.title = getString(R.string.new_plant_toolbar)
        binding.plant = plantViewModel.newPlant
    }
}