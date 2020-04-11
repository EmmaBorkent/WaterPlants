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
        setPlantDataToViews()
    }

    private fun setEditActionBarTitle() {
        supportActionBar?.setTitle(R.string.edit_plant_toolbar)
    }

    private fun setPlantDataToViews() {
        val plant = getPlantFromDatabase()
        // TODO: 10-4-2020 Change number fields to date fields
        edit_plant_name.setText(plant.name)
        edit_plant_species.setText(plant.species)
        image_plant.setImageURI(Uri.parse(plant.image))
        edit_date_plants_needs_water.setText(plant.datePlantNeedsWater.toString())
        edit_water_every_days.setText(plant.daysToNextWater.toString())
        edit_date_plants_needs_mist.setText(plant.datePlantNeedsMist.toString())
        edit_mist_every_days.setText(plant.daysToNextMist.toString())
    }

    private fun getPlantFromDatabase(): Plant {
        return PlantDatabaseHandler(this).readPlant(plantId())
    }

    private fun plantId(): Int {
        return intent.getIntExtra("PLANT_ID", 0)
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
            R.id.action_save_new -> saveNewPlantToDatabase()
            R.id.action_save_update -> updatePlantInDatabase()
            R.id.action_delete -> deletePlantFromDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNewPlantToDatabase() {
        if (!hasEmptyTextFields() && hasImageSelected()) {
            PlantDatabaseHandler(this).createPlant(newPlant())
        }
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
        val defaultImage = resources.getDrawable(R.drawable.ic_image_black_24dp)
        defaultImage
        if (image_plant.drawable == defaultImage) {
            warnToSelectImage()
            Log.d("ADD_EDIT", "There is no new image selected")
            return false
        }

        Log.d("ADD_EDIT", "There is a new image selected")
        return true
    }

    private fun warnToSelectImage() {
        Toast.makeText(this, "Please select an image for this plant",
            Toast.LENGTH_LONG).show()
    }

    private fun newPlant(): Plant {
        val plantImageUri = saveImageToInternalStorage()

        val plant = Plant()
        plant.name = edit_plant_name.text.toString()
        plant.species = edit_plant_species.text.toString()
        plant.image = plantImageUri.toString()
        plant.datePlantNeedsWater = edit_date_plants_needs_water.text.toString().toLong()
        plant.daysToNextWater = edit_water_every_days.text.toString().toLong()
        plant.datePlantNeedsMist = edit_date_plants_needs_mist.text.toString().toLong()
        plant.daysToNextMist = edit_mist_every_days.text.toString().toLong()
        return plant
    }

    private fun saveImageToInternalStorage(): Uri {
        // Get the image from drawable resource as drawable object
        val drawable = image_plant.drawable as BitmapDrawable
        // Get the bitmap from drawable object
        val bitmap = drawable.bitmap
        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)
        // Initializing a new file
        // The below line returns a directory in internal storage
        var file = wrapper.getDir("plant_images", Context.MODE_PRIVATE)
        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // Return the saved image URI
        return Uri.parse(file.absolutePath)
    }

    private fun updatePlantInDatabase() {
        // TODO: 11-4-2020 implement updatePlantInDatabase function
        Toast.makeText(this, "Update Plant", Toast.LENGTH_SHORT).show()
    }

    private fun deletePlantFromDatabase() {
        // TODO: 11-4-2020 implement deletePlantFromDatabase function
        Toast.makeText(this, "Delete Plant", Toast.LENGTH_SHORT).show()
    }

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

    private fun openImageGallery() {
        val openImageGallery = Intent(Intent.ACTION_GET_CONTENT)
        openImageGallery.type = "image/*"
        startActivityForResult(openImageGallery, PICK_IMAGE_CODE)
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

    // Function to set the chosen image as the background of the imageView
    // TODO: 10-4-2020 Either remove block of code or decide to use an image button
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_CODE) {
            image_plant.setImageURI(data?.data)

            // Use this when an imageButton is used
//            new_plant_image_button.setImageURI(data?.data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}