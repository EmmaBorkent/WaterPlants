package com.emmaborkent.waterplants.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.Plant
import com.emmaborkent.waterplants.database.PlantDatabaseHandler
import kotlinx.android.synthetic.main.activity_add_edit_plant.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddEditPlantActivity : AppCompatActivity() {

    private var databaseHandler = PlantDatabaseHandler(this)
    private var plant = Plant()
    private var imageIsChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_plant)
        setSupportActionBar(findViewById(R.id.new_plant_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupPageContent()
    }

    private fun setupPageContent() {
        if (isEditActivity())
            setupPageToEditPlant()
        setupPageToAddPlant()
    }

    private fun isEditActivity(): Boolean {
        return intent.hasExtra("PLANT_ID")
    }

    private fun setupPageToEditPlant() {
        setEditActionBarTitle()
        getPlantFromDatabase()
        setPlantDataToViews()
    }

    private fun setEditActionBarTitle() {
        supportActionBar?.setTitle(R.string.edit_plant_toolbar)
    }

    private fun getPlantFromDatabase() {
        plant = databaseHandler.readPlant(plantId())
    }

    private fun plantId(): Int {
        return intent.getIntExtra("PLANT_ID", 0)
    }

    private fun setPlantDataToViews() {
        // TODO: 10-4-2020 Change number fields to date fields
        edit_plant_name.setText(plant.name)
        edit_plant_species.setText(plant.species)
        image_plant.setImageURI(Uri.parse(plant.image))
        button_date_plants_needs_water.text = plant.datePlantNeedsWater.toString()
        edit_water_every_days.setText(plant.daysToNextWater.toString())
        edit_date_plants_needs_mist.setText(plant.datePlantNeedsMist.toString())
        edit_mist_every_days.setText(plant.daysToNextMist.toString())
    }

    private fun setupPageToAddPlant() {
        setAddActionBarTitle()
    }

    private fun setAddActionBarTitle() {
        supportActionBar?.setTitle(R.string.new_plant_toolbar)
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
            R.id.action_save_new -> createNewPlantAndSaveToDatabase()
            R.id.action_save_update -> updatePlantInDatabase()
            R.id.action_delete -> deletePlantFromDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNewPlantAndSaveToDatabase() {
        if (!hasEmptyTextFields() && hasImageSelected()) {
            createPlantFromFields()
            databaseHandler.savePlantToDatabase(plant)
            goBackToHomeScreen()
        }
    }

    private fun updatePlantInDatabase() {
        if (!hasEmptyTextFields()) {
            updatePlantFromFields()
            databaseHandler.updatePlantInDatabase(plant)
            goBackToHomeScreen()
        }
    }

    private fun deletePlantFromDatabase() {
        databaseHandler.deletePlant(plantId())
        goBackToHomeScreen()
    }

    private fun hasEmptyTextFields(): Boolean {
        if (edit_plant_name.text.isBlank() || edit_plant_species.text.isBlank()) {
            warnForEmptyFields()
            return true
        }
        return false
    }

    private fun warnForEmptyFields() {
        Toast.makeText(this, R.string.new_plant_save_toast, Toast.LENGTH_SHORT).show()
    }

    private fun hasImageSelected(): Boolean {
        if (!imageIsChanged) {
            warnToSelectImage()
            return false
        }
        return true
    }

    private fun warnToSelectImage() {
        Toast.makeText(
            this, "Please select an image for this plant",
            Toast.LENGTH_LONG
        ).show()
    }

    // TODO: 14-4-2020 Make only name OR species a required field
    private fun createPlantFromFields() {
        val plantImageUri = saveNewImageToInternalStorage()
        plant.image = plantImageUri.toString()
        plant.name = edit_plant_name.text.toString()
        plant.species = edit_plant_species.text.toString()
        setOtherPlantFieldsIfTheyExist(plant)
    }

    private fun updatePlantFromFields() {
        plant.id = plantId()
        if (imageIsChanged) {
            val plantImageUri = saveNewImageToInternalStorage()
            plant.image = plantImageUri.toString()
        }
        plant.name = edit_plant_name.text.toString()
        plant.species = edit_plant_species.text.toString()
        setOtherPlantFieldsIfTheyExist(plant)
    }

    private fun setOtherPlantFieldsIfTheyExist(plant: Plant) {
        if (button_date_plants_needs_water.text !== R.string.new_plant_text_water_date.toString()) {
            // TODO: 16-4-2020 button_date_plants_needs_water.text.toString().toLong() is wrong and needs to be changed to date
            plant.datePlantNeedsWater = button_date_plants_needs_water.text.toString().toLong()
        }
        if (edit_water_every_days.text.isNotEmpty()) {
            plant.daysToNextWater = edit_water_every_days.text.toString().toLong()
        }
        if (edit_date_plants_needs_mist.text.isNotEmpty()) {
            plant.datePlantNeedsMist = edit_date_plants_needs_mist.text.toString().toLong()
        }
        if (edit_mist_every_days.text.toString().isNotEmpty()) {
            plant.daysToNextMist = edit_mist_every_days.text.toString().toLong()
        }
    }

    // TODO: 12-4-2020 Improve function, it is too long and has too many comments
    private fun saveNewImageToInternalStorage(): Uri {
        val drawable = image_plant.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("plant_images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)
            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            // Flush the stream
            stream.flush()
            // Close the stream
            stream.close()
        } catch (e: IOException) {
            // Catch the exception
            e.printStackTrace()
        }

        Log.d("ADD_EDIT", "New Image saved")
        // Return the saved image URI
        return Uri.parse(file.absolutePath)
    }

    private fun goBackToHomeScreen() {
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
    }

    // TODO: 13-4-2020 improve pickImage() function
    // TODO: 16-4-2020 change image to image button and remove pick image button, change image to add picture image
    @Suppress("UNUSED_PARAMETER")
    fun pickImage(view: View) {
        // Check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                // If permission is denied
                val permissions = arrayOf(
                    android.Manifest.permission
                        .READ_EXTERNAL_STORAGE
                )
                // Show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                // If permission is granted
                openImageGallery()
            }
        } else {
            // System OS is < Marshmallow
            openImageGallery()
        }
    }

    companion object {
        const val PICK_IMAGE_CODE = 1000
        const val PERMISSION_CODE = 1001
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
            Log.d("ADD_EDIT", "imageChanged status now is $imageIsChanged")
            image_plant.setImageURI(data?.data)

            // Use this when an imageButton is used
            // new_plant_image_button.setImageURI(data?.data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // TODO: 16-4-2020 change datePicker tag
    @Suppress("UNUSED_PARAMETER")
    fun showDatePickerDialog(view: View) {
        val datePicker = DatePickerFragment()
        datePicker.show(supportFragmentManager, "datePicker")
    }
}