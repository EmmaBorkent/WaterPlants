package com.emmaborkent.waterplants.view

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
import androidx.lifecycle.ViewModelProvider
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.viewmodel.ParseFormatDates
import com.emmaborkent.waterplants.viewmodel.PlantViewModel
import kotlinx.android.synthetic.main.activity_add_edit_plant.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDate
import java.util.*

class AddEditPlantActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private val classNameTag: String = AddEditPlantActivity::class.java.simpleName

    private lateinit var plantViewModel: PlantViewModel

    private lateinit var editPlantName: EditText
    private lateinit var editPlantSpecies: EditText
    private lateinit var imagePlant: ImageView
    private lateinit var buttonPlantWaterDate: Button
    private lateinit var editPlantWaterEveryDays: EditText
    private lateinit var buttonPlantMistDate: Button
    private lateinit var editPlantMistEveryDays: EditText

    private var imageIsChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_plant)
//        setSupportActionBar(findViewById(R.id.new_plant_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editPlantName = findViewById(R.id.edit_plant_name)
        editPlantSpecies = findViewById(R.id.edit_plant_species)
        imagePlant = findViewById(R.id.image_plant)
        buttonPlantWaterDate = findViewById(R.id.button_date_plants_needs_water)
        editPlantWaterEveryDays = findViewById(R.id.edit_water_every_days)
        buttonPlantMistDate = findViewById(R.id.button_date_plants_needs_mist)
        editPlantMistEveryDays = findViewById(R.id.edit_mist_every_days)

        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        setupPageContent()
        // TODO: 16-4-2020 Shouldn't I have used onClick method in XML? that way you always need
        //  to pass the view
        button_date_plants_needs_water.setOnClickListener {
            showDatePickerDialog(button_date_plants_needs_water)
        }
        button_date_plants_needs_mist.setOnClickListener {
            showDatePickerDialog(button_date_plants_needs_mist)
        }
    }

    private fun setupPageContent() {
        if (isEditActivity()) {
//            setupPageToEditPlant()
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
        getPlantFromDatabase()
        setPlantDataToViews()
    }

    private fun setEditActionBarTitle() {
        supportActionBar?.setTitle(R.string.edit_plant_toolbar)
    }

    private fun getPlantFromDatabase() {
//        plant = dbHandler.readPlant(plantId())
//        dbHandler.close()
    }

    // TODO: 25-6-2020 Remove plantId function, plantId should be shared through the PlantViewModel
    private fun plantId(): Int {
        return intent.getIntExtra("PLANT_ID", 0)
    }

    private fun setPlantDataToViews() {
//        edit_plant_name.setText(plant.name)
//        edit_plant_species.setText(plant.species)
//        image_plant.setImageURI(Uri.parse(plant.image))
//        button_date_plants_needs_water.text = ParseFormatDates()
//            .yearMonthDayStringToDayMonthYearString(plant.datePlantNeedsWater)
//        edit_water_every_days.setText(plant.daysToNextWater)
//        button_date_plants_needs_mist.text = ParseFormatDates()
//            .yearMonthDayStringToDayMonthYearString(plant.datePlantNeedsMist)
//        edit_mist_every_days.setText(plant.daysToNextMist)
    }

    private fun setupPageToAddPlant() {
        setAddActionBarTitle()
        setTodayDateToDateButtons()
    }

    private fun setAddActionBarTitle() {
        supportActionBar?.setTitle(R.string.new_plant_toolbar)
    }

    // TODO: 1-5-2020 Might not be necessary  when this is done in Plant Class
    private fun setTodayDateToDateButtons() {
        val localDate = LocalDate.now()
        val dateAsString = ParseFormatDates()
            .dateToStringLocalized(localDate)
        button_date_plants_needs_water.text = dateAsString
        button_date_plants_needs_mist.text = dateAsString
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
            R.id.action_save_new -> savePlant()
            R.id.action_save_update -> updatePlantInDatabase()
            R.id.action_delete -> deletePlantFromDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savePlant() {
        val plantImageUri = saveNewImageToInternalStorage()

        val name = editPlantName.text.toString()
        val species = editPlantSpecies.text.toString()
        val image = plantImageUri.toString()
        val waterEveryDays = editPlantWaterEveryDays.text.toString().toInt()
        val waterDate =
            ParseFormatDates().dayMonthYearStringToYearMonthDayString(buttonPlantWaterDate.text.toString())
        val mistEveryDays = editPlantMistEveryDays.text.toString().toInt()
        val mistDate =
            ParseFormatDates().dayMonthYearStringToYearMonthDayString(buttonPlantMistDate.text.toString())

        val replyIntent = Intent()
        if (hasEmptyTextViews()) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
            return
        } else {
            replyIntent.putExtra(EXTRA_REPLY_NAME, name)
            replyIntent.putExtra(EXTRA_REPLY_SPECIES, species)
            replyIntent.putExtra(EXTRA_REPLY_IMAGE, image)
            replyIntent.putExtra(EXTRA_REPLY_WATER_EVERY_DAYS, waterEveryDays)
            replyIntent.putExtra(EXTRA_REPLY_WATER_DATE, waterDate)
            replyIntent.putExtra(EXTRA_REPLY_MIST_DATE, mistDate)
            replyIntent.putExtra(EXTRA_REPLY_MIST_EVERY_DAYS, mistEveryDays)
            setResult(Activity.RESULT_OK, replyIntent)
        }
        finish()
    }

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
        if (editPlantName.text!!.isBlank() || editPlantSpecies.text!!.isBlank()) {
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
        val drawable = imagePlant.drawable as BitmapDrawable
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

    @Suppress("UNUSED_PARAMETER")
    fun pickImage(view: View) {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_DENIED
        ) {
            val permissions = arrayOf(
                android.Manifest.permission
                    .READ_EXTERNAL_STORAGE
            )
            requestPermissions(permissions, PERMISSION_CODE)
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
        startActivityForResult(openImageGallery, PICK_IMAGE_CODE)
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
        val datePicker = DatePickerFragment()
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