package com.emmaborkent.waterplants

import android.app.Application
import androidx.lifecycle.*
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.model.PlantDatabase
import com.emmaborkent.waterplants.model.PlantRepository
import com.emmaborkent.waterplants.util.DateConverter
import kotlinx.coroutines.*
import timber.log.Timber
import java.time.LocalDate
import java.time.Period

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlantRepository
    val plantsThatNeedWater: LiveData<List<Plant>>
    val plantsThatNeedMist: LiveData<List<Plant>>
    private val countPlantsThatNeedWater: LiveData<Int>
    private val countPlantsThatNeedMist: LiveData<Int>
    val countAllPlantsThatNeedWaterOrMist: LiveData<Int>
    val allPlants: LiveData<List<Plant>>
    private var parseFormatDates: DateConverter

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    fun updateActionBarTitle(title: String) = _title.postValue(title)

    // Coroutines
    private var viewModelJob = Job()
    // Using Dispatchers.Main means that coroutines launched in the uiScope will run on the main
    // thread. This is sensible for many coroutines started by a ViewModel, because after these
    // coroutines perform some processing, they result in an update of the UI.
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var plant = MutableLiveData<Plant>()

    init {
        val plantDao = PlantDatabase.getDatabaseInstance(application).plantDao
        repository = PlantRepository(plantDao)
//        plantId = savedStateHandle[PLANT_ID] ?:
//                throw kotlin.IllegalArgumentException("Missing Plant ID")
//        plant = repository.getPlant(plantId)
        parseFormatDates = DateConverter.getDateConverterInstance()
        plantsThatNeedWater = repository.getPlantsThatNeedWater()
        plantsThatNeedMist = repository.getPlantsThatNeedMist()
        countPlantsThatNeedWater = repository.countPlantsThatNeedWater()
        countPlantsThatNeedMist = repository.countPlantsThatNeedMist()
        countAllPlantsThatNeedWaterOrMist = repository.countAllPlantsThatNeedWaterOrMist()

        allPlants = repository.getAllPlants()

        Timber.i("PlantViewModel created")
    }

    fun initializePlant(plantId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            plant.value = repository.getPlant(plantId)
        }
        Timber.i("Plant Initialized")
    }

    private fun update(plant: Plant) {
        uiScope.launch {
            repository.update(plant)
        }
    }

    // TODO: 26-7-2020 Apply DataBinding with ViewModel and DataBinding to all functions and views
    fun giveWater(plant: Plant) {
        Timber.i("giveWater datePlantNeedsWater was ${plant.waterDate}")
        val todayDate = LocalDate.now()
        plant.waterInDays = Period.between(
            plant.waterDate,
            todayDate
        ).days
        Timber.i("giveWater daysBetweenDateAndToday is ${plant.waterInDays}")
        val nextWaterDate = todayDate.plusDays(plant.waterEveryDays.toLong())
        plant.waterDate = nextWaterDate
        Timber.i("giveWater datePlantNeedsWater is ${plant.waterDate}")
        update(plant)
    }

    fun undoWaterGift(plant: Plant) {
        Timber.i("undoWaterGift datePlantNeedsWater was ${plant.waterDate}")
        val days = plant.waterEveryDays.toLong() + plant.waterInDays.toLong()
        Timber.i("undoWaterGift days is $days")
        val previousWaterDate = plant.waterDate.minusDays(days)
        plant.waterDate = previousWaterDate
        Timber.i("undoWaterGift datePlantNeedsWater is ${plant.waterDate}")
        update(plant)
    }

    fun giveMist(plant: Plant) {
        Timber.i("giveMist datePlantNeedsMist was ${plant.mistDate}")
        val todayDate = LocalDate.now()
        plant.mistInDays = Period.between(
            plant.mistDate,
            todayDate
        ).days
        Timber.i("giveMist daysBetweenDateAndToday is ${plant.mistInDays}")
        val nextMistDate = todayDate.plusDays(plant.mistEveryDays.toLong())
        plant.mistDate = nextMistDate
        Timber.i("giveMist datePlantNeedsMist is ${plant.mistDate}")
        update(plant)
    }

    fun undoMistGift(plant: Plant) {
        Timber.i("undoMistGift datePlantNeedsMist was ${plant.mistDate}")
        val days = plant.mistEveryDays.toLong() + plant.mistInDays.toLong()
        Timber.i("undoMistGift days is $days")
        val previousMistDate = plant.mistDate.minusDays(days)
        plant.mistDate = previousMistDate
        Timber.i("undoMistGift datePlantNeedsMist is ${plant.mistDate}")
        update(plant)
    }

    // Not necessary to cancel or instantiate a Job when using ViewModelScope, because they get
    // canceled when the viewModel ends. onCleared only used for lifecycle tracking.
    override fun onCleared() {
        super.onCleared()
        Timber.i("PlantViewModel destroyed")
    }
}