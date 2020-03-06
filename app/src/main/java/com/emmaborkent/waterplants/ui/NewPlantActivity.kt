package com.emmaborkent.waterplants.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emmaborkent.waterplants.R

class NewPlantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_plant)
        setSupportActionBar(findViewById(R.id.new_plant_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.new_plant_toolbar)
    }
}
