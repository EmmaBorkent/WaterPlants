package com.emmaborkent.waterplants.main

import android.app.Application
import androidx.lifecycle.*
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.model.PlantDatabase
import com.emmaborkent.waterplants.model.PlantRepository
import com.emmaborkent.waterplants.util.ParseFormatDates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    val plantsThatNeedWater: LiveData<List<Plant>>
    val plantsThatNeedMist: LiveData<List<Plant>>
    val allPlants: LiveData<List<Plant>>
    val selectedPlant = MutableLiveData<Plant>()

    init {
        val plantDao = PlantDatabase.getDatabaseInstance(application).plantDao()
        repository = PlantRepository(plantDao)
//        plantId = savedStateHandle[PLANT_ID] ?:
//                throw kotlin.IllegalArgumentException("Missing Plant ID")
//        plant = repository.getPlant(plantId)
        plantsThatNeedWater = repository.getPlantsThatNeedWater()
        plantsThatNeedMist = repository.getPlantsThatNeedMist()
        allPlants = repository.getAllPlants()
    }

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

    // TODO: 19-6-2020 check correctness of function
    fun giveWater(plant: Plant) {
//        Log.d(classNameTag, "giveWater datePlantNeedsWater was ${plant.datePlantNeedsWater}")
        val todayDate = LocalDate.now()
        plant.waterInDays = Period.between(
            ParseFormatDates().stringToDateDefault(plant.waterDate),
            todayDate
        ).days.toString()
//        Log.d(classNameTag, "giveWater daysBetweenDateAndToday is ${plant.daysBetweenDateAndToday}")
        val nextWaterDate = todayDate.plusDays(plant.waterEveryDays.toLong())
        plant.waterDate = ParseFormatDates()
            .dateToStringDefault(nextWaterDate)
//        Log.d(classNameTag, "giveWater datePlantNeedsWater is ${plant.datePlantNeedsWater}")

//                dbHandler.updatePlantInDatabase(plant)
    }

    fun undoWaterGift(plant: Plant) {
//        Log.d(classNameTag, "undoWaterGift datePlantNeedsWater was ${plant.datePlantNeedsWater}")
        val days = plant.waterEveryDays.toLong() + plant.waterInDays.toLong()
//        Log.d(classNameTag, "undoWaterGift days is $days")
        val previousWaterDate =
            ParseFormatDates().stringToDateDefault(plant.waterDate).minusDays(days)
        plant.waterDate = ParseFormatDates()
            .dateToStringDefault(previousWaterDate)
//        Log.d(classNameTag, "undoWaterGift datePlantNeedsWater is ${plant.datePlantNeedsWater}")
    }

    fun giveMist(plant: Plant) {
        val todayDate = LocalDate.now()
        plant.mistInDays = Period.between(
            ParseFormatDates().stringToDateDefault(plant.mistDate),
            todayDate
        ).days.toString()
        val nextMistDate = todayDate.plusDays(plant.mistEveryDays.toLong())
        plant.mistDate = ParseFormatDates()
            .dateToStringDefault(nextMistDate)
    }

    fun undoMistGift(plant: Plant) {
        val days = plant.mistEveryDays.toLong() + plant.mistInDays.toLong()
        val previousMistDate =
            ParseFormatDates().stringToDateDefault(plant.mistDate).minusDays(days)
        plant.mistDate = ParseFormatDates()
            .dateToStringDefault(previousMistDate)
    }
}