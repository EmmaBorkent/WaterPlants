package com.emmaborkent.waterplants.plantdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.model.PlantDatabase
import com.emmaborkent.waterplants.model.PlantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailsViewModel(private var plantId: Int, application: Application)
    : AndroidViewModel(application) {

    private val repository: PlantRepository
    val plant = MutableLiveData<Plant>()

    init {
        Timber.i("DetailsViewModel Created")

        val plantDao = PlantDatabase.getDatabaseInstance(application).plantDao
        repository = PlantRepository(plantDao)
        initializePlant()
    }

    private fun initializePlant() {
        viewModelScope.launch(Dispatchers.Main) {
            plant.value = repository.getPlant(plantId)
        }
        Timber.i("Plant Initialized")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("DetailsViewModel Destroyed")
    }
}