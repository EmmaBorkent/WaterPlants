package com.emmaborkent.waterplants.plantstoday

//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import com.emmaborkent.waterplants.model.Plant
//import com.emmaborkent.waterplants.model.PlantDatabase
//import com.emmaborkent.waterplants.model.PlantRepository
//import com.emmaborkent.waterplants.util.ParseFormatDates
//import timber.log.Timber
//import java.time.LocalDate
//import java.time.Period
//
//class PlantsTodayViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository: PlantRepository
//    val plantsThatNeedWater: LiveData<List<Plant>>
//    val plantsThatNeedMist: LiveData<List<Plant>>
//
//    init {
//        val plantDao = PlantDatabase.getDatabaseInstance(application).plantDao()
//        repository = PlantRepository(plantDao)
//        plantsThatNeedWater = repository.getPlantsThatNeedWater()
//        plantsThatNeedMist = repository.getPlantsThatNeedMist()
//        Timber.i("PlantsTodayViewModel created")
//    }
//
//    // TODO: 19-6-2020 check correctness of function
//    fun giveWater(plant: Plant) {
////        Timber.i("giveWater datePlantNeedsWater was ${plant.datePlantNeedsWater}")
//        val todayDate = LocalDate.now()
//        plant.waterInDays = Period.between(
//            ParseFormatDates().stringToDateDefault(plant.waterDate),
//            todayDate
//        ).days
////        Timber.i("giveWater daysBetweenDateAndToday is ${plant.daysBetweenDateAndToday}")
//        val nextWaterDate = todayDate.plusDays(plant.waterEveryDays.toLong())
//        plant.waterDate = ParseFormatDates()
//            .dateToStringDefault(nextWaterDate)
////        Timber.i("giveWater datePlantNeedsWater is ${plant.datePlantNeedsWater}")
//
////                dbHandler.updatePlantInDatabase(plant)
//    }
//
//    fun undoWaterGift(plant: Plant) {
////        Timber.i("undoWaterGift datePlantNeedsWater was ${plant.datePlantNeedsWater}")
//        val days = plant.waterEveryDays.toLong() + plant.waterInDays.toLong()
////        Timber.i("undoWaterGift days is $days")
//        val previousWaterDate =
//            ParseFormatDates().stringToDateDefault(plant.waterDate).minusDays(days)
//        plant.waterDate = ParseFormatDates()
//            .dateToStringDefault(previousWaterDate)
////        Timber.i("undoWaterGift datePlantNeedsWater is ${plant.datePlantNeedsWater}")
//    }
//
//    fun giveMist(plant: Plant) {
//        val todayDate = LocalDate.now()
//        plant.mistInDays = Period.between(
//            ParseFormatDates().stringToDateDefault(plant.mistDate),
//            todayDate
//        ).days
//        val nextMistDate = todayDate.plusDays(plant.mistEveryDays.toLong())
//        plant.mistDate = ParseFormatDates()
//            .dateToStringDefault(nextMistDate)
//    }
//
//    fun undoMistGift(plant: Plant) {
//        val days = plant.mistEveryDays.toLong() + plant.mistInDays.toLong()
//        val previousMistDate =
//            ParseFormatDates().stringToDateDefault(plant.mistDate).minusDays(days)
//        plant.mistDate = ParseFormatDates()
//            .dateToStringDefault(previousMistDate)
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        Timber.i("PlantsTodayViewModel destroyed")
//    }
//
//}