package com.emmaborkent.waterplants.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.emmaborkent.waterplants.model.PlantDatabase
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.model.PlantRepository

class PlantsTodayViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PlantRepository
    val plantsThatNeedWater: LiveData<List<Plant>>
    val plantsThatNeedMist: LiveData<List<Plant>>

    init {
        val plantDao = PlantDatabase.getDatabaseInstance(application).plantDao()
        repository = PlantRepository(plantDao)
        plantsThatNeedWater = repository.getPlantsThatNeedWater()
        plantsThatNeedMist = repository.getPlantsThatNeedMist()
    }
}