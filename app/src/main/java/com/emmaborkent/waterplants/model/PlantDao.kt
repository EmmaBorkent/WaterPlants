package com.emmaborkent.waterplants.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlantDao {

    @Insert
    fun insert(vararg plant: PlantEntity)

    @Update
    fun updatePlant(vararg plant: PlantEntity)

    @Delete
    fun deletePlant(vararg plant: PlantEntity)

    // TODO: 5-6-2020 Async queries with Kotlin coroutines
    //  https://developer.android.com/training/data-storage/room/accessing-data#kotlin-coroutines

    @Query("DELETE FROM PLANT_TABLE")
    fun deleteAllPlants()

    @Query("SELECT * FROM PLANT_TABLE ORDER BY id DESC")
    fun getAllPlants(): LiveData<List<PlantEntity>>

    @Query("SELECT * FROM PLANT_TABLE WHERE waterDate <= Date(:dateAsYearMonthDay)")
    fun getPlantsThatNeedWater(dateAsYearMonthDay: String): LiveData<List<PlantEntity>>

    @Query("SELECT * FROM PLANT_TABLE WHERE mistDate <= Date(:dateAsYearMonthDay)")
    fun getPlantsThatNeedMist(dateAsYearMonthDay: String): LiveData<List<PlantEntity>>

}