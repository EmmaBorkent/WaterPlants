package com.emmaborkent.waterplants

import android.app.Application
import androidx.lifecycle.*
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.model.PlantDatabase
import com.emmaborkent.waterplants.model.PlantRepository
import com.emmaborkent.waterplants.util.ParseFormatDates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.Period

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val VIEW_TYPE_WATER = 0
        const val VIEW_TYPE_MIST = 1
    }

    private val repository: PlantRepository
//    private val plantId: Int
//    val plant: LiveData<Plant>
    val selectedPlant = MutableLiveData<Plant>()
    private lateinit var parseFormatDates: ParseFormatDates

    init {
        val plantDao = PlantDatabase.getDatabaseInstance(application).plantDao()
        repository = PlantRepository(plantDao)
//        plantId = savedStateHandle[PLANT_ID] ?:
//                throw kotlin.IllegalArgumentException("Missing Plant ID")
//        plant = repository.getPlant(plantId)
        parseFormatDates = ParseFormatDates.getParseFormatDatesInstance()

        Timber.i("PlantViewModel created")
    }

    val testPlant: Plant = Plant(
        "TestPlant",
        "Test",
        R.drawable.ic_image_black_24dp.toString(),
        "2",
        "2020-07-09",
        "3",
        "2020-07-09"
    )

    val newPlant: Plant = Plant(
        "",
        "",
        R.drawable.ic_image_black_24dp.toString(),
        "",
        parseFormatDates.getTodayDateAsString(),
        "",
        parseFormatDates.getTodayDateAsString()
    )

    fun select(plant: Plant) {
        selectedPlant.value = plant
    }

    fun insert(plant: Plant) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(plant)
    }

    fun update(plant: Plant) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(plant)
    }

    fun delete(plant: Plant) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(plant)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun getPlant(id: Int) {
        repository.getPlant(id)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("PlantViewModel destroyed")
    }
}