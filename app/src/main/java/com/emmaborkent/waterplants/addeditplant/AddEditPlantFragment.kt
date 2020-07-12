package com.emmaborkent.waterplants.addeditplant

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentAddEditPlantBinding
import com.emmaborkent.waterplants.main.PlantViewModel
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.util.ParseFormatDates
import kotlinx.android.synthetic.main.activity_add_edit_plant.*
import java.time.LocalDate

class AddEditPlantFragment : Fragment() {

    private val classNameTag: String = AddEditPlantActivity::class.java.simpleName
    private lateinit var binding: FragmentAddEditPlantBinding
    private val plant: Plant = Plant(
        "TestPlant",
        "Test",
        R.drawable.ic_image_black_24dp.toString(),
        "2",
        "2020-07-09",
        "3",
        "2020-07-09"
    )
    private lateinit var plantViewModel: PlantViewModel
    private var imageIsChanged = false
    private lateinit var clickedButtonView: Button

    companion object {
        const val EXTRA_REPLY_NAME = "com.emmaborkent.waterplants.REPLY_NAME"
        const val EXTRA_REPLY_SPECIES = "com.emmaborkent.waterplants.REPLY_SPECIES"
        const val EXTRA_REPLY_IMAGE = "com.emmaborkent.waterplants.REPLY_IMAGE"
        const val EXTRA_REPLY_WATER_DATE = "com.emmaborkent.waterplants.REPLY_WATER_DATE"
        const val EXTRA_REPLY_WATER_EVERY_DAYS =
            "com.emmaborkent.waterplants.REPLY_WATER_EVERY_DAYS"
        const val EXTRA_REPLY_MIST_DATE = "com.emmaborkent.waterplants.REPLY_MIST_DATE"
        const val EXTRA_REPLY_MIST_EVERY_DAYS = "com.emmaborkent.waterplants.REPLY_MIST_EVERY_DAYS"

        const val PICK_IMAGE_CODE = 1000
        const val PERMISSION_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_add_edit_plant, container, false)
        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        setupPageContent()
        // TODO: 16-4-2020 Shouldn't I have used onClick method in XML? that way you always need
        //  to pass the view
        binding.buttonDatePlantsNeedsWater.setOnClickListener {
            showDatePickerDialog(button_date_plants_needs_water)
        }
        binding.buttonDatePlantsNeedsMist.setOnClickListener {
            showDatePickerDialog(button_date_plants_needs_mist)
        }
        return binding.root
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
            DatePickerFragment()
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
    private fun isEditActivity(): Boolean {
//        return intent.hasExtra("PLANT_ID")
        return false
    }

    // TODO: 25-6-2020 Set function setupPageToEditPlant()
    private fun setupPageToEditPlant() {
        setEditActionBarTitle()
        binding.plant = plant
//        getPlantFromDatabase()
//        setPlantDataToViews()
    }

    // TODO: 12-7-2020 How to set action bar title from fragment?
    private fun setEditActionBarTitle() {
//        supportActionBar?.setTitle(R.string.edit_plant_toolbar)
    }

    // TODO: 12-7-2020 How to set action bar title from fragment?
    private fun setupPageToAddPlant() {
//        supportActionBar?.setTitle(R.string.new_plant_toolbar)
        setTodayDateToDateButtons()
    }

    // TODO: 1-5-2020 Might not be necessary to set today date here when this is done in Plant Class
    private fun setTodayDateToDateButtons() {
        val localDate = LocalDate.now()
        val dateAsString = ParseFormatDates()
            .dateToStringLocalized(localDate)

        // When using data binging with plant it is not possible anymore to set button text with
        // binding.button.text like this: binding.buttonDatePlantsNeedsMist.text = dateAsString
        val addNewPlant: Plant = Plant(
            "",
            "",
            R.drawable.ic_image_black_24dp.toString(),
            "",
            dateAsString,
            "",
            dateAsString
        )
        binding.plant = addNewPlant
    }

}