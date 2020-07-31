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
    private var _selectedPlant = MutableLiveData<Plant>()
    val selectedPlant: LiveData<Plant>
        get() = _selectedPlant
    val plantsThatNeedWater: LiveData<List<Plant>>
    val plantsThatNeedMist: LiveData<List<Plant>>
    val allPlants: LiveData<List<Plant>>
    private var parseFormatDates: ParseFormatDates

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    fun updateActionBarTitle(title: String) = _title.postValue(title)

    init {
        val plantDao = PlantDatabase.getDatabaseInstance(application).plantDao()
        repository = PlantRepository(plantDao)
//        plantId = savedStateHandle[PLANT_ID] ?:
//                throw kotlin.IllegalArgumentException("Missing Plant ID")
//        plant = repository.getPlant(plantId)
        parseFormatDates = ParseFormatDates.getParseFormatDatesInstance()
        plantsThatNeedWater = repository.getPlantsThatNeedWater()
        plantsThatNeedMist = repository.getPlantsThatNeedMist()
        allPlants = repository.getAllPlants()

        Timber.i("PlantViewModel created")
    }

    val testPlant: Plant = Plant(
        "TestPlant",
        "Test",
        R.drawable.ic_image_black_24dp.toString(),
        1,
        "2020-07-09",
        3,
        "2020-07-09"
    )

    val newPlant: Plant = Plant(
        "",
        "",
        R.drawable.ic_image_black_24dp.toString(),
        1,
        parseFormatDates.getTodayDateAsString(),
        1,
        parseFormatDates.getTodayDateAsString()
    )

    fun select(plant: Plant) {
        _selectedPlant.value = plant
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

    fun countAllPlantsThatNeedWaterOrMist(): Int {
        return 0
    }

    // TODO: 26-7-2020 Apply DataBinding with ViewModel and DataBinding to all functions and views
    // TODO: 19-6-2020 check correctness of function
    fun giveWater(plant: Plant) {
//        Timber.i("giveWater datePlantNeedsWater was ${plant.datePlantNeedsWater}")
        val todayDate = LocalDate.now()
        plant.waterInDays = Period.between(
            ParseFormatDates().stringToDateDefault(plant.waterDate),
            todayDate
        ).days
//        Timber.i("giveWater daysBetweenDateAndToday is ${plant.daysBetweenDateAndToday}")
        val nextWaterDate = todayDate.plusDays(plant.waterEveryDays.toLong())
        plant.waterDate = ParseFormatDates()
            .dateToStringDefault(nextWaterDate)
//        Timber.i("giveWater datePlantNeedsWater is ${plant.datePlantNeedsWater}")

//                dbHandler.updatePlantInDatabase(plant)
    }

    fun undoWaterGift(plant: Plant) {
//        Timber.i("undoWaterGift datePlantNeedsWater was ${plant.datePlantNeedsWater}")
        val days = plant.waterEveryDays.toLong() + plant.waterInDays.toLong()
//        Timber.i("undoWaterGift days is $days")
        val previousWaterDate =
            ParseFormatDates().stringToDateDefault(plant.waterDate).minusDays(days)
        plant.waterDate = ParseFormatDates()
            .dateToStringDefault(previousWaterDate)
//        Timber.i("undoWaterGift datePlantNeedsWater is ${plant.datePlantNeedsWater}")
    }

    fun giveMist(plant: Plant) {
        val todayDate = LocalDate.now()
        plant.mistInDays = Period.between(
            ParseFormatDates().stringToDateDefault(plant.mistDate),
            todayDate
        ).days
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

    override fun onCleared() {
        super.onCleared()
        Timber.i("PlantViewModel destroyed")
    }
}