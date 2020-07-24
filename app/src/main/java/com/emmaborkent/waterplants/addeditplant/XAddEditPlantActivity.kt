package com.emmaborkent.waterplants.addeditplant

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.emmaborkent.waterplants.main.MainActivity
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.util.ParseFormatDates
import com.emmaborkent.waterplants.databinding.ActivityAddEditPlantBinding
import com.emmaborkent.waterplants.PlantViewModel
import com.emmaborkent.waterplants.model.Plant
import kotlinx.android.synthetic.main.activity_add_edit_plant.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDate
import java.util.*

class XAddEditPlantActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val classNameTag: String = XAddEditPlantActivity::class.java.simpleName
    private lateinit var binding: ActivityAddEditPlantBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_plant)
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
    }

    private fun setupPageContent() {
        if (isEditActivity()) {
            setupPageToEditPlant()
        } else {
            setupPageToAddPlant()
        }
    }

    private fun isEditActivity(): Boolean {
        return intent.hasExtra("PLANT_ID")
    }

    // TODO: 25-6-2020 Set function setupPageToEditPlant()
    private fun setupPageToEditPlant() {
        setEditActionBarTitle()
        binding.plant = plant
//        getPlantFromDatabase()
//        setPlantDataToViews()
    }

    private fun setEditActionBarTitle() {
        supportActionBar?.setTitle(R.string.edit_plant_toolbar)
    }

//    private fun getPlantFromDatabase() {
////        plant = dbHandler.readPlant(plantId())
////        dbHandler.close()
//    }

    // TODO: 25-6-2020 Remove plantId function, plantId should be shared through the PlantViewModel
    private fun plantId(): Int {
        return intent.getIntExtra("PLANT_ID", 0)
    }

    private fun setPlantDataToViews() {
        binding.apply {

//            editPlantName.setText(plant.name.toString())
//            editPlantSpecies.setText(plant.species.toString())
//            imagePlant.setImageURI(Uri.parse(plant.image))
//            buttonDatePlantsNeedsWater.text = ParseFormatDates()
//                .yearMonthDayStringToDayMonthYearString(plant.datePlantNeedsWater)
//            editWaterEveryDays.setText(plant.daysToNextWater.toString())
//            buttonDatePlantsNeedsMist.text = ParseFormatDates()
//                .yearMonthDayStringToDayMonthYearString(plant.datePlantNeedsMist)
//            editMistEveryDays.setText(plant.daysToNextMist.toString())
        }
    }

    private fun setupPageToAddPlant() {
        supportActionBar?.setTitle(R.string.new_plant_toolbar)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_add_edit_plant, menu)
        if (isEditActivity()) {
            val saveNewPlantMenuItem = menu!!.findItem(R.id.action_save_new)
            saveNewPlantMenuItem.isVisible = false
            val updatePlantMenuItem = menu.findItem(R.id.action_save_update)
            updatePlantMenuItem.isVisible = true
            val deleteMenuItem: MenuItem = menu.findItem(R.id.action_delete)
            deleteMenuItem.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // TODO: 9-7-2020 There should be only one savePlant function used from the view model
            R.id.action_save_new -> savePlant()
            R.id.action_save_update -> updatePlantInDatabase()
            R.id.action_delete -> deletePlantFromDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savePlant() {
        val plantImageUri = saveNewImageToInternalStorage()

//        binding.apply {
//            plant?.name = editPlantName.text.toString()
//            plant?.species = editPlantSpecies.text.toString()
//            plant?.image = plantImageUri.toString()
//            plant?.waterEveryDays = editWaterEveryDays.text.toString()
//            plant?.waterDate = ParseFormatDates().dayMonthYearStringToYearMonthDayString(buttonDatePlantsNeedsWater.text.toString())
//            plant?.mistEveryDays = editMistEveryDays.text.toString()
//            plant?.mistDate = ParseFormatDates().dayMonthYearStringToYearMonthDayString(buttonDatePlantsNeedsMist.text.toString())
//        }

        val replyIntent = Intent()
        if (hasEmptyTextViews()) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
            return
        } else {
            binding.apply {
                replyIntent.putExtra(EXTRA_REPLY_NAME, editPlantName.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_SPECIES, editPlantSpecies.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_IMAGE, plantImageUri.toString())
                replyIntent.putExtra(
                    EXTRA_REPLY_WATER_EVERY_DAYS,
                    editWaterEveryDays.text.toString()
                )
                replyIntent.putExtra(
                    EXTRA_REPLY_WATER_DATE,
                    ParseFormatDates().dayMonthYearStringToYearMonthDayString(
                        buttonDatePlantsNeedsWater.text.toString()
                    )
                )
                replyIntent.putExtra(EXTRA_REPLY_MIST_DATE, editMistEveryDays.text.toString())
                replyIntent.putExtra(
                    EXTRA_REPLY_MIST_EVERY_DAYS,
                    ParseFormatDates().dayMonthYearStringToYearMonthDayString(
                        buttonDatePlantsNeedsMist.text.toString()
                    )
                )
                setResult(Activity.RESULT_OK, replyIntent)
            }

//            replyIntent.putExtra(EXTRA_REPLY_NAME, binding.editPlantName.text.toString())
//            replyIntent.putExtra(EXTRA_REPLY_SPECIES, binding.editPlantSpecies.text.toString())
//            replyIntent.putExtra(EXTRA_REPLY_IMAGE, plantImageUri.toString())
//            replyIntent.putExtra(EXTRA_REPLY_WATER_EVERY_DAYS, binding.editWaterEveryDays.text.toString())
//            replyIntent.putExtra(EXTRA_REPLY_WATER_DATE, ParseFormatDates().dayMonthYearStringToYearMonthDayString(binding.buttonDatePlantsNeedsWater.text.toString()))
//            replyIntent.putExtra(EXTRA_REPLY_MIST_DATE, binding.editMistEveryDays.text.toString())
//            replyIntent.putExtra(EXTRA_REPLY_MIST_EVERY_DAYS, ParseFormatDates().dayMonthYearStringToYearMonthDayString(binding.buttonDatePlantsNeedsMist.text.toString()))
//            setResult(Activity.RESULT_OK, replyIntent)
        }
        finish()
    }

    private fun updatePlantInDatabase() {
//        if (!hasEmptyTextViews()) {
        updatePlantFromViews()
//            dbHandler.updatePlantInDatabase(plant)
//            dbHandler.close()
//            goBackToHomeScreen()
//        }
    }

    // TODO: 16-4-2020 create warning before deleting plant
    private fun deletePlantFromDatabase() {
//        dbHandler.deletePlant(plantId())
//        dbHandler.close()
//        goBackToHomeScreen()
    }

    private fun hasEmptyTextViews(): Boolean {
        if (binding.editPlantName.text!!.isBlank() || binding.editPlantSpecies.text!!.isBlank()) {
            warnForEmptyViews()
            return true
        }
        return false
    }

    private fun warnForEmptyViews() {
        Toast.makeText(this, R.string.new_plant_save_toast, Toast.LENGTH_SHORT).show()
    }

    private fun hasImageSelected(): Boolean {
        if (!imageIsChanged) {
            warnToSelectImage()
            return false
        }
        return true
    }

    // TODO: 17-4-2020 Create string for toast text warnToSelectImage()
    private fun warnToSelectImage() {
        Toast.makeText(
            this, "Please select an image for this plant",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun createPlantFromViews() {
//        val plantImageUri = saveNewImageToInternalStorage()
//        plant.apply {
//            name = edit_plant_name.text.toString()
//            species = edit_plant_species.text.toString()
//            image = plantImageUri.toString()
//            datePlantNeedsWater = ParseFormatDates()
//                .dayMonthYearStringToYearMonthDayString(button_date_plants_needs_water.text.toString())
//            daysToNextWater = edit_water_every_days.text.toString().toInt()
//            datePlantNeedsMist = ParseFormatDates()
//                .dayMonthYearStringToYearMonthDayString(button_date_plants_needs_mist.text.toString())
//            daysToNextMist = edit_mist_every_days.text.toString().toInt()
//            Log.d(classNameTag, "Plan water date: ${plant.datePlantNeedsWater}, needs water today? ${plant.needsWater}")
//            Log.d(classNameTag, "Plan mist date: ${plant.datePlantNeedsMist}, needs mist today? ${plant.needsMist}")
//        }
    }

    private fun updatePlantFromViews() {
//        plant.id = plantId()
//        if (imageIsChanged) {
//            val plantImageUri = saveNewImageToInternalStorage()
//            plant.image = plantImageUri.toString()
//        }
//        plant.apply {
//            name = edit_plant_name.text.toString()
//            species = edit_plant_species.text.toString()
//            datePlantNeedsWater = ParseFormatDates()
//                .dayMonthYearStringToYearMonthDayString(button_date_plants_needs_water.text.toString())
//            daysToNextWater = edit_water_every_days.text.toString().toInt()
//            datePlantNeedsMist = ParseFormatDates()
//                .dayMonthYearStringToYearMonthDayString(button_date_plants_needs_mist.text.toString())
//            daysToNextMist = edit_mist_every_days.text.toString().toInt()
//            Log.d(classNameTag, "Plant water date: ${plant.datePlantNeedsWater}, needs water today? ${plant.needsWater}")
//            Log.d(classNameTag, "Plant mist date: ${plant.datePlantNeedsMist}, needs mist today? ${plant.needsMist}")
//        }
    }

    private fun saveNewImageToInternalStorage(): Uri {
        val drawable = binding.imagePlant.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("plant_images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        val stream: OutputStream = FileOutputStream(file)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)
            stream.flush()
        } finally {
            try {
                stream.close()
            } catch (e: IOException) {
                Log.e(classNameTag, "Closing OutputStream Failed")
            }
        }

        Log.d(classNameTag, "New Image saved")
        return Uri.parse(file.absolutePath)
    }

    private fun goBackToHomeScreen() {
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
    }

    // TODO: 9-7-2020 Why has this view: View as parameter and suppress Unused Parameter?
    @Suppress("UNUSED_PARAMETER")
    fun pickImage(view: View) {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_DENIED
        ) {
            val permissions = arrayOf(
                android.Manifest.permission
                    .READ_EXTERNAL_STORAGE
            )
            requestPermissions(
                permissions,
                PERMISSION_CODE
            )
        } else {
            openImageGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager
                        .PERMISSION_DENIED
                ) {
                    // Permission from popup is denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                } else {
                    // Permission from popup is granted
                    openImageGallery()
                }
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
            imageIsChanged = true
            Log.d(classNameTag, "imageChanged status now is $imageIsChanged")
            image_plant.setImageURI(data?.data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private lateinit var clickedButtonView: Button

    @Suppress("UNUSED_PARAMETER")
    fun showDatePickerDialog(clickedButton: Button) {
        clickedButtonView = clickedButton
        val datePicker =
            DatePickerDialogFragment()
        if (isEditActivity()) {
            val args = Bundle()
            // TODO: 23-4-2020 Create key value pairs for Bundles and Intents
            //  see 2.2.13 String constants, naming, and values
            args.putString("DATE", clickedButton.text.toString())
            datePicker.arguments = args
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = ParseFormatDates()
            .yearMonthDayToDate(year, month, dayOfMonth)
        val dateString = ParseFormatDates()
            .dateToStringLocalized(date)
        clickedButtonView.text = dateString
    }
}