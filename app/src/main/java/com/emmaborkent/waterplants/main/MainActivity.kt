package com.emmaborkent.waterplants.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.emmaborkent.waterplants.PlantViewModel
import com.emmaborkent.waterplants.R
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PlantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i("onCreate Called")
        setContentView(R.layout.activity_main)
        val navControllerMain = this.findNavController(R.id.nav_host_container_main)
        NavigationUI.setupActionBarWithNavController(this, navControllerMain)

        viewModel = ViewModelProvider(this).get(PlantViewModel::class.java)
        viewModel.title.observe(this, Observer {
            supportActionBar?.title = it
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_container_main)
        return navController.navigateUp()
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart Called")
    }
}