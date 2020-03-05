package com.emmaborkent.waterplants

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewPlantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_plant)
        setTitle(R.string.new_plant_title)
    }
}
