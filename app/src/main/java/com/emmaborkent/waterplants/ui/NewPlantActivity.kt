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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.Plant
import com.emmaborkent.waterplants.database.PlantDatabaseHandler
import kotlinx.android.synthetic.main.activity_new_plant.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class NewPlantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_plant)
        setSupportActionBar(findViewById(R.id.new_plant_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.new_plant_toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> saveNewPlant()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNewPlant() {

        if (new_plant_name_edit.text.isBlank() || new_plant_species_edit.text.isBlank() ||
            new_plant_date_edit.text.isBlank() || new_plant_repeat_edit.text.isBlank()) {

            val toast = Toast.makeText(this, R.string.new_plant_save_toast,
                Toast.LENGTH_SHORT)
            toast.show()

        } else {

            if (checkImageResource(this, new_plant_image)) {
                Toast.makeText(this, "Please select an image for this plant",
                    Toast.LENGTH_LONG).show()
            } else {
                createPlant()
            }

            val toast = Toast.makeText(this, "Saved! ;)",
                Toast.LENGTH_SHORT)
            toast.show()

        }
    }

    private fun createPlant() {
        val newPlantImage = saveImageToInternalStorage()

        val plant = Plant()
        plant.name = new_plant_name_edit.text.toString()
        plant.species = new_plant_species_edit.text.toString()
        plant.image = newPlantImage.toString()
        plant.endDate = new_plant_date_edit.text.toString().toLong()
        plant.repeat = new_plant_repeat_edit.text.toString().toLong()
        PlantDatabaseHandler(this).createPlant(plant)
    }

    @Suppress("DEPRECATION")
    private fun checkImageResource(context: Context, imageView: ImageView):
            Boolean {
        var result = false

        if (imageView.drawable != null) {
            val constantState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.resources.getDrawable(R.drawable.ic_image_black_24dp, context.theme).constantState
            } else {
                context.resources.getDrawable(R.drawable.ic_image_black_24dp).constantState
            }

            if (imageView.drawable.constantState == constantState) {
                result = true
            }
        }

        return result
    }

    companion object {
        // Pick image code
        const val PICK_IMAGE_CODE = 1000
        // Permission code
        const val PERMISSION_CODE = 1001
    }

    private fun openImageGallery() {
        // Intent to open the image gallery
        val setImageIntent = Intent(Intent.ACTION_GET_CONTENT)
        setImageIntent.type = "image/*"
        startActivityForResult(setImageIntent, PICK_IMAGE_CODE)
    }

    @Suppress("UNUSED_PARAMETER")
    fun pickImage(view: View) {
        val toast = Toast.makeText(this, "Opening Image Gallery",
            Toast.LENGTH_SHORT)
        toast.show()

        // Check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                // If permission is denied
                val permissions = arrayOf(android.Manifest.permission
                    .READ_EXTERNAL_STORAGE)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager
                        .PERMISSION_DENIED) {
                    // Permission from popup is granted
                    openImageGallery()
                } else {
                    // Permission from popup is denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_CODE) {
            new_plant_image.setImageURI(data?.data)

            // Use this when an imageButton is used
//            new_plant_image_button.setImageURI(data?.data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Function to save an image to internal storage
    private fun saveImageToInternalStorage(): Uri {
        // Get the image from drawable resource as drawable object
        val drawable = new_plant_image.drawable as BitmapDrawable

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

        // Return the saved image URI
        return Uri.parse(file.absolutePath)
    }
}