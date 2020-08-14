package com.emmaborkent.waterplants.addeditplant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emmaborkent.waterplants.model.DateConverter
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.model.PlantDatabase
import com.emmaborkent.waterplants.model.PlantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

class AddEditPlantViewModel(private var plantId: Int, application: Application) : AndroidViewModel(application) {

    private val repository: PlantRepository
    private val dateConverter: DateConverter
    var plant = MutableLiveData<Plant>()

    private val _waterDate = MutableLiveData<String>()
    val waterDate: LiveData<String>
        get() = _waterDate
    var waterYear = MutableLiveData<Int>()
    var waterMonth = MutableLiveData<Int>()
    var waterDay = MutableLiveData<Int>()

    private val _mistDate = MutableLiveData<String>()
    val mistDate: LiveData<String>
        get() = _mistDate
    var mistYear = MutableLiveData<Int>()
    var mistMonth = MutableLiveData<Int>()
    var mistDay = MutableLiveData<Int>()

    init {
        Timber.i("AddEditPlantViewModel Created")

        val plantDao = PlantDatabase.getDatabaseInstance(application).plantDao
        repository = PlantRepository(plantDao)
        dateConverter = DateConverter.getDateConverterInstance()
    }

    fun setupPageToAddPlant() {
        viewModelScope.launch(Dispatchers.Main) {
            val newPlant = Plant()
            repository.insert(newPlant)
            plant.value = repository.getLatestPlant()
            initializeDates()
        }
    }

    fun setupPageToEditPlant() {
        viewModelScope.launch(Dispatchers.Main) {
            plant.value = repository.getPlant(plantId)
            initializeDates()
        }
    }

    private fun initializeDates() {
        _waterDate.value = dateConverter.localDateToViewDateString(plant.value?.waterDate!!)
        waterYear.value = plant.value!!.waterDate.year
        waterMonth.value = plant.value!!.waterDate.monthValue
        waterDay.value = plant.value!!.waterDate.dayOfMonth

        _mistDate.value = dateConverter.localDateToViewDateString(plant.value?.mistDate!!)
        mistYear.value = plant.value!!.mistDate.year
        mistMonth.value = plant.value!!.mistDate.monthValue
        mistDay.value = plant.value!!.mistDate.dayOfMonth
    }
    
    fun changeWaterDate(year: Int, month: Int, day: Int) {
        val date = LocalDate.of(year, month, day)
        val dateString = dateConverter.localDateToViewDateString(date)
        _waterDate.value = dateString
    }

    fun changeMistDate(year: Int, month: Int, day: Int) {
        val date = LocalDate.of(year, month, day)
        val dateString = dateConverter.localDateToViewDateString(date)
        _mistDate.value = dateString
    }

    fun update(plant: Plant) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.update(plant)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("AddEditPlantViewModel Destroyed")
    }
}