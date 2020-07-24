package com.emmaborkent.waterplants.allplants

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.model.PlantDatabase
import com.emmaborkent.waterplants.model.PlantRepository
import timber.log.Timber

class PlantsAllViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlantRepository
    val allPlants: LiveData<List<Plant>>

    init {
        val plantDao = PlantDatabase.getDatabaseInstance(application).plantDao()
        repository = PlantRepository(plantDao)
        Timber.i("PlantsAllViewModel created")
        allPlants = repository.getAllPlants()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("PlantsAllViewModel destroyed")
    }
}