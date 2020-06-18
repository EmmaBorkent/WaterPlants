package com.emmaborkent.waterplants.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.emmaborkent.waterplants.model.PLANT_ID

class PlantDetailsViewModel(
    savedStateHandle: SavedStateHandle, application: Application
) : AndroidViewModel(application) {

//    private val repository: PlantRepository

    private val plantId: Int = savedStateHandle[PLANT_ID] ?:
            throw kotlin.IllegalArgumentException("Missing Plant ID")
//    val plant: LiveData<Plant> = plantRepository.getPlant(plantId)

    var daysToNextWater: Int = 0
    var daysToNextMist: Int = 0
}