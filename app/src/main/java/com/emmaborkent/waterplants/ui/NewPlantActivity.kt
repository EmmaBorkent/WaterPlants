package com.emmaborkent.waterplants.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.Plant
import com.emmaborkent.waterplants.database.PlantDatabaseHandler
import kotlinx.android.synthetic.main.activity_new_plant.*

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

            val toast = Toast.makeText(this, "Saved! ;)",
                Toast.LENGTH_SHORT)
            toast.show()


            val plant = Plant()

            PlantDatabaseHandler(this).createPlant(plant)

        }
    }
}