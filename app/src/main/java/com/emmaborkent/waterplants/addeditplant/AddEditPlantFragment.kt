package com.emmaborkent.waterplants.addeditplant

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentAddEditPlantBinding
import com.emmaborkent.waterplants.util.PERMISSION_CODE
import com.emmaborkent.waterplants.util.PICK_IMAGE_CODE
import timber.log.Timber
import java.time.LocalDate
import java.time.Period

class AddEditPlantFragment : Fragment() {

    private lateinit var binding: FragmentAddEditPlantBinding
    private lateinit var viewModelFactory: AddEditPlantViewModelFactory
    private lateinit var viewModel: AddEditPlantViewModel
    private var isEditActivity: Boolean = false

    private lateinit var clickedButtonView: View
    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            when (clickedButtonView) {
                binding.buttonDatePlantsNeedsWater -> {
                    viewModel.changeWaterDate(year, month, dayOfMonth)
                }
                binding.buttonDatePlantsNeedsMist -> {
                    viewModel.changeMistDate(year, month, dayOfMonth)
                }
            }
        }


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
        val plantId = AddEditPlantFragmentArgs.fromBundle(requireArguments()).plantId
        val application = requireNotNull(this.activity).application
        viewModelFactory = AddEditPlantViewModelFactory(plantId, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddEditPlantViewModel::class.java)
        binding.lifecycleOwner = this
        binding.addEditPlantViewModel = viewModel

        isEditActivity = isEditActivity(plantId)
        setHasOptionsMenu(true)

        if (isEditActivity) {
            setActivityTitle(R.string.edit_plant_toolbar_title)
            viewModel.setupPageToEditPlant()
        } else {
            setActivityTitle(R.string.add_plant_toolbar_title)
            viewModel.setupPageToAddPlant()
        }

        binding.buttonPickImage.setOnClickListener {
            pickImage()
        }
        binding.buttonDatePlantsNeedsWater.setOnClickListener { v ->
            clickedButtonView = v!!
            if (viewModel.plant.value != null) {
                DatePickerDialog(requireContext(), dateSetListener,
                    viewModel.waterYear.value!!,
                    viewModel.waterMonth.value!!,
                    viewModel.waterDay.value!!)
                    .show()
            } else {
                throw IllegalArgumentException("No value for date")
            }
        }
        binding.buttonDatePlantsNeedsMist.setOnClickListener { v ->
            clickedButtonView = v!!
            if (viewModel.plant.value != null) {
                DatePickerDialog(requireContext(), dateSetListener,
                    viewModel.mistYear.value!!,
                    viewModel.mistMonth.value!!,
                    viewModel.mistDay.value!!)
                    .show()
            } else {
                throw IllegalArgumentException("No value for date")
            }
        }

        return binding.root
    }

    private fun isEditActivity(plantId: Int): Boolean {
        return plantId != 0
    }

    private fun Fragment.setActivityTitle(id: Int) {
        (activity as AppCompatActivity?)!!.supportActionBar?.title = getString(id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu_add_edit_plant, menu)
        if (isEditActivity) {
            val saveNewPlantMenuItem = menu.findItem(R.id.action_save_new)
            saveNewPlantMenuItem.isVisible = false
            val updatePlantMenuItem = menu.findItem(R.id.action_save_update)
            updatePlantMenuItem.isVisible = true
            val deleteMenuItem: MenuItem = menu.findItem(R.id.action_delete)
            deleteMenuItem.isVisible = true
        }
    }

    private fun pickImage() {
        if (checkSelfPermission(requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, PERMISSION_CODE)
        } else {
            openImageGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager
                    .PERMISSION_DENIED) {
                // Permission from popup is denied
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            } else {
                // Permission from popup is granted
                openImageGallery()
            }
        }
    }

    private fun openImageGallery() {
        val openImageGallery = Intent(Intent.ACTION_GET_CONTENT)
        openImageGallery.type = "image/*"
        startActivityForResult(
            openImageGallery,
            PICK_IMAGE_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_CODE) {
            // TODO: 13-8-2020 Implement functionality
//            imageIsChanged = true
//            image_plant.setImageURI(data?.data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // TODO: 9-7-2020 There should be only one savePlant function used from the view model
            R.id.action_save_new -> {
                updatePlant()
                view?.findNavController()
                    ?.navigate(AddEditPlantFragmentDirections.actionAddEditPlantFragmentToTabbedFragment())
            }
            R.id.action_save_update -> {
                updatePlant()
                view?.findNavController()?.navigate(
                    AddEditPlantFragmentDirections.actionAddEditPlantFragmentToDetailsFragment(1)
                )
            }
            R.id.action_delete -> {
                // are you sure?
                // delete plant
                // TODO: 13-8-2020 Create function
                viewModel.deletePlant(viewModel.plant.value!!)
                view?.findNavController()
                    ?.navigate(AddEditPlantFragmentDirections.actionAddEditPlantFragmentToTabbedFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updatePlant() {
        viewModel.plant.value?.apply {
            name = binding.editPlantName.text.toString()
            species = binding.editPlantSpecies.text.toString()
            // TODO: 14-8-2020 Set Plant Image parameter
            waterEveryDays = binding.editWaterEveryDays.text.toString().toInt()
            waterDate = viewModel.plant.value?.waterDate!!
            mistEveryDays = binding.editMistEveryDays.text.toString().toInt()
            mistDate = viewModel.plant.value?.mistDate!!

            val today = LocalDate.now()
            waterInDays = Period.between(today, waterDate).days
            mistInDays = Period.between(today, mistDate).days
        }

        viewModel.updatePlant(viewModel.plant.value)
    }


    //    private fun setupPageToAddNewPlant() {
//        // TODO: 13-8-2020 Setting the title should happen earlier, because now there is a lag and
//        //  the old title can be seen first
//        val title = getString(R.string.new_plant_toolbar)
//        viewModel.updateActionBarTitle(title)
//
//        val newPlant = Plant()
//        viewModel.insert(newPlant)
//
//
//
//        val todayDate = LocalDate.now()
//        binding.buttonDatePlantsNeedsWater.text = dateConverter.localDateToViewDateString(todayDate)
//        binding.buttonDatePlantsNeedsMist.text = dateConverter.localDateToViewDateString(todayDate)
//    }

//    private fun setupPageToEditPlant() {
//        val title: String = getString(R.string.edit_plant_toolbar)
//        viewModel.updateActionBarTitle(title)
//        viewModel.initializePlant(plantId)
//        binding.plant = viewModel.plant.value
//
////        val testPlant = Plant()
////        testPlant.apply {
////            name = "TestPlant"
////            species = "Test"
////            waterEveryDays = 2
////        }
////        binding.plant = testPlant
//    }

    // Old Code From Deleted Activity

//    private fun savePlant() {
//        val plantImageUri = saveNewImageToInternalStorage()
//
////        binding.apply {
////            plant?.name = editPlantName.text.toString()
////            plant?.species = editPlantSpecies.text.toString()
////            plant?.image = plantImageUri.toString()
////            plant?.waterEveryDays = editWaterEveryDays.text.toString()
////            plant?.waterDate = ParseFormatDates().dayMonthYearStringToYearMonthDayString(buttonDatePlantsNeedsWater.text.toString())
////            plant?.mistEveryDays = editMistEveryDays.text.toString()
////            plant?.mistDate = ParseFormatDates().dayMonthYearStringToYearMonthDayString(buttonDatePlantsNeedsMist.text.toString())
////        }
//
//        val replyIntent = Intent()
//        if (hasEmptyTextViews()) {
//            setResult(Activity.RESULT_CANCELED, replyIntent)
//            return
//        } else {
//            binding.apply {
//                replyIntent.putExtra(EXTRA_REPLY_NAME, editPlantName.text.toString())
//                replyIntent.putExtra(EXTRA_REPLY_SPECIES, editPlantSpecies.text.toString())
//                replyIntent.putExtra(EXTRA_REPLY_IMAGE, plantImageUri.toString())
//                replyIntent.putExtra(
//                    EXTRA_REPLY_WATER_EVERY_DAYS,
//                    editWaterEveryDays.text.toString()
//                )
//                replyIntent.putExtra(
//                    EXTRA_REPLY_WATER_DATE,
//                    ParseFormatDates().dayMonthYearStringToYearMonthDayString(
//                        buttonDatePlantsNeedsWater.text.toString()
//                    )
//                )
//                replyIntent.putExtra(EXTRA_REPLY_MIST_DATE, editMistEveryDays.text.toString())
//                replyIntent.putExtra(
//                    EXTRA_REPLY_MIST_EVERY_DAYS,
//                    ParseFormatDates().dayMonthYearStringToYearMonthDayString(
//                        buttonDatePlantsNeedsMist.text.toString()
//                    )
//                )
//                setResult(Activity.RESULT_OK, replyIntent)
//            }
//
////            replyIntent.putExtra(EXTRA_REPLY_NAME, binding.editPlantName.text.toString())
////            replyIntent.putExtra(EXTRA_REPLY_SPECIES, binding.editPlantSpecies.text.toString())
////            replyIntent.putExtra(EXTRA_REPLY_IMAGE, plantImageUri.toString())
////            replyIntent.putExtra(EXTRA_REPLY_WATER_EVERY_DAYS, binding.editWaterEveryDays.text.toString())
////            replyIntent.putExtra(EXTRA_REPLY_WATER_DATE, ParseFormatDates().dayMonthYearStringToYearMonthDayString(binding.buttonDatePlantsNeedsWater.text.toString()))
////            replyIntent.putExtra(EXTRA_REPLY_MIST_DATE, binding.editMistEveryDays.text.toString())
////            replyIntent.putExtra(EXTRA_REPLY_MIST_EVERY_DAYS, ParseFormatDates().dayMonthYearStringToYearMonthDayString(binding.buttonDatePlantsNeedsMist.text.toString()))
////            setResult(Activity.RESULT_OK, replyIntent)
//        }
//        finish()
//    }
//
//    private fun hasEmptyTextViews(): Boolean {
//        if (binding.editPlantName.text!!.isBlank() || binding.editPlantSpecies.text!!.isBlank()) {
//            warnForEmptyViews()
//            return true
//        }
//        return false
//    }
//
//    private fun warnForEmptyViews() {
//        Toast.makeText(this, R.string.new_plant_save_toast, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun hasImageSelected(): Boolean {
//        if (!imageIsChanged) {
//            warnToSelectImage()
//            return false
//        }
//        return true
//    }
//
//    // TODO: 17-4-2020 Create string for toast text warnToSelectImage()
//    private fun warnToSelectImage() {
//        Toast.makeText(
//            this, "Please select an image for this plant",
//            Toast.LENGTH_LONG
//        ).show()
//    }
//private fun saveNewImageToInternalStorage(): Uri {
//    val drawable = binding.imagePlant.drawable as BitmapDrawable
//    val bitmap = drawable.bitmap
//    val wrapper = ContextWrapper(applicationContext)
//    var file = wrapper.getDir("plant_images", Context.MODE_PRIVATE)
//    file = File(file, "${UUID.randomUUID()}.jpg")
//    val stream: OutputStream = FileOutputStream(file)
//    try {
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)
//        stream.flush()
//    } finally {
//        try {
//            stream.close()
//        } catch (e: IOException) {
//            Log.e(classNameTag, "Closing OutputStream Failed")
//        }
//    }
//
//    Log.d(classNameTag, "New Image saved")
//    return Uri.parse(file.absolutePath)
//}

}