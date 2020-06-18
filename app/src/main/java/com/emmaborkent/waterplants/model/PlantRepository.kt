package com.emmaborkent.waterplants.model

import androidx.lifecycle.LiveData
import com.emmaborkent.waterplants.viewmodel.ParseFormatDates

class PlantRepository(private val plantDao: PlantDao) {

    private var parseFormatDates = ParseFormatDates.getParseFormatDatesInstance()
    private val allPlants: LiveData<List<PlantEntity>> = plantDao.getAllPlants()
    private val plantsThatNeedWater: LiveData<List<PlantEntity>> =
        plantDao.getPlantsThatNeedWater(parseFormatDates.getDefaultDateAsString())
    private val plantsThatNeedMist: LiveData<List<PlantEntity>> =
        plantDao.getPlantsThatNeedMist(parseFormatDates.getDefaultDateAsString())

    // The suspend modifiers tell the compiler that they need to be called from a coroutine
    // or another suspending function.
    // This is not needed for the functions that return LiveData, because LiveData automatically
    // takes care of this.

    suspend fun insert(plant: PlantEntity) {
        plantDao.insert(plant)
    }

    suspend fun update(plant: PlantEntity) {
        plantDao.updatePlant(plant)
    }

    suspend fun delete(plant: PlantEntity) {
        plantDao.deletePlant(plant)
    }

    suspend fun deleteAll() {
        plantDao.deleteAllPlants()
    }

    fun getAllPlants(): LiveData<List<PlantEntity>> {
        return allPlants
    }

    fun getPlantsThatNeedWater(): LiveData<List<PlantEntity>> {
        return plantsThatNeedWater
    }

    fun getPlantsThatNeedMist(): LiveData<List<PlantEntity>> {
        return plantsThatNeedMist
    }

//    private val databaseHandler: PlantDatabaseHandler = PlantDatabaseHandler.getInstance(MainActivity())
//
//    fun getPlant(plantId: Int): LiveData<Plant> {
//        val plant = MutableLiveData<Plant>()
//        databaseHandler.readPlant(plantId)
//        return plant
//    }
//
//    fun getAllPlants(): ArrayList<Plant> {
//        return databaseHandler.readAllPlants()
//    }
//
//    fun getAllPlantsThatNeedWaterOrMist(): ArrayList<Plant> {
//        val plantsThatNeedWater = getPlantsThatNeedWater()
//        val plantsThatNeedMist = getPlantsThatNeedMist()
//
//        for (plant in plantsThatNeedWater) {
//            plant.needsWater = true
//            plant.needsMist = false
//        }
//
//        for (plant in plantsThatNeedMist) {
//            plant.needsWater = false
//            plant.needsMist = true
//        }
//
//        val allPlantsThatNeedWaterOrMist = ArrayList<Plant>()
//        allPlantsThatNeedWaterOrMist.addAll(plantsThatNeedWater)
//        allPlantsThatNeedWaterOrMist.addAll(plantsThatNeedMist)
//        return allPlantsThatNeedWaterOrMist
//    }
//
//    private fun getPlantsThatNeedWater(): ArrayList<Plant> {
//        return databaseHandler.getPlantsThatNeedWater(ParseFormatDates().getDefaultDateAsString())
//    }
//
//    private fun getPlantsThatNeedMist(): ArrayList<Plant> {
//        return databaseHandler.getPlantsThatNeedMist(ParseFormatDates().getDefaultDateAsString())
//    }

}