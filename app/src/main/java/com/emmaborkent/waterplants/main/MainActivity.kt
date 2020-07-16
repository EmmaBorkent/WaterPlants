package com.emmaborkent.waterplants.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.emmaborkent.waterplants.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navControllerMain = this.findNavController(R.id.nav_host_container_main)
        NavigationUI.setupActionBarWithNavController(this, navControllerMain)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_container_main)
        return navController.navigateUp()
    }
}