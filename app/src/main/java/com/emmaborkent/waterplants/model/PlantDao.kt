package com.emmaborkent.waterplants.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlantDao {

    @Insert
    fun insert(vararg plant: Plant)

    @Update
    fun updatePlant(vararg plant: Plant)

    @Delete
    fun deletePlant(vararg plant: Plant)

    // TODO: 5-6-2020 Async queries with Kotlin coroutines
    //  https://developer.android.com/training/data-storage/room/accessing-data#kotlin-coroutines

    @Query("DELETE FROM PLANT_TABLE")
    fun deleteAllPlants()

    @Query("SELECT * FROM PLANT_TABLE WHERE id=:id")
    fun getPlant(id: Int): Plant

    @Query("SELECT * FROM PLANT_TABLE ORDER BY id DESC")
    fun getAllPlants(): LiveData<List<Plant>>

    @Query("SELECT * FROM PLANT_TABLE WHERE waterDate <= Date(:dateAsYearMonthDay)")
    fun getPlantsThatNeedWater(dateAsYearMonthDay: String): LiveData<List<Plant>>

    @Query("SELECT * FROM PLANT_TABLE WHERE mistDate <= Date(:dateAsYearMonthDay)")
    fun getPlantsThatNeedMist(dateAsYearMonthDay: String): LiveData<List<Plant>>

    @Query("SELECT COUNT(*) FROM PLANT_TABLE WHERE waterDate <= Date(:dateAsYearMonthDay)")
    fun countPlantsThatNeedWater(dateAsYearMonthDay: String): LiveData<Int>

    @Query("SELECT COUNT(*) FROM PLANT_TABLE WHERE mistDate <= Date(:dateAsYearMonthDay)")
    fun countPlantsThatNeedMist(dateAsYearMonthDay: String): LiveData<Int>

}