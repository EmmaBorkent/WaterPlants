package com.emmaborkent.waterplants.addeditplant

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class AddEditPlantViewModelFactory(private val plantId: Int, private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditPlantViewModel::class.java)) {
            return AddEditPlantViewModel(plantId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}