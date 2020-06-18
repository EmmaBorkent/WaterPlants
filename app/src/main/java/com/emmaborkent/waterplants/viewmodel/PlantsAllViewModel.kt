package com.emmaborkent.waterplants.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.emmaborkent.waterplants.model.PlantDatabase
import com.emmaborkent.waterplants.model.PlantEntity
import com.emmaborkent.waterplants.model.PlantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlantsAllViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PlantRepository
    val allPlants: LiveData<List<PlantEntity>>

    init {
        val plantDao = PlantDatabase.getDatabaseInstance(application).plantDao()
        repository = PlantRepository(plantDao)
        allPlants = repository.getAllPlants()
    }

    fun insert(plant: PlantEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(plant)
    }

    fun update(plant: PlantEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(plant)
    }

    fun delete(plant: PlantEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(plant)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

//    fun giveWater(plant: Plant) {
////        Log.d(classNameTag, "giveWater datePlantNeedsWater was ${plant.datePlantNeedsWater}")
//        val todayDate = LocalDate.now()
//        plant.daysBetweenDateAndToday = Period.between(
//            ParseFormatDates().stringToDateDefault(plant.datePlantNeedsWater),
//            todayDate
//        ).days
////        Log.d(classNameTag, "giveWater daysBetweenDateAndToday is ${plant.daysBetweenDateAndToday}")
//        val nextWaterDate = todayDate.plusDays(plant.daysToNextWater.toLong())
//        plant.datePlantNeedsWater = ParseFormatDates().dateToStringDefault(nextWaterDate)
////        Log.d(classNameTag, "giveWater datePlantNeedsWater is ${plant.datePlantNeedsWater}")
//    }
}