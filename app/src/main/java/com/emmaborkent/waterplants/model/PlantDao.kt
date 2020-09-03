package com.emmaborkent.waterplants.model

import androidx.lifecycle.LiveData
import androidx.room.*
import java.time.LocalDate

@Dao
@TypeConverters(DateConverter::class)
interface PlantDao {

    @Insert
    fun insert(vararg plant: Plant)

    @Update
    fun update(vararg plant: Plant?)

    @Delete
    fun deletePlant(vararg plant: Plant)

    // TODO: 5-6-2020 Async queries with Kotlin coroutines
    //  https://developer.android.com/training/data-storage/room/accessing-data#kotlin-coroutines

    @Query("SELECT * FROM PLANT_TABLE ORDER BY id DESC LIMIT 1")
    fun getLatestPlant(): Plant?

    @Query("SELECT * FROM PLANT_TABLE WHERE id=:id")
    fun getPlant(id: Int): Plant?

    @Query("SELECT * FROM PLANT_TABLE ORDER BY id DESC")
    fun getAllPlants(): LiveData<List<Plant>>

    @Query("SELECT COUNT(*) FROM PLANT_TABLE")
    fun countAllPlants(): Int

//    @Query("SELECT * FROM PLANT_TABLE WHERE water_date <= Date(:dateAsYearMonthDay)")
//    fun getPlantsThatNeedWater(dateAsYearMonthDay: String): LiveData<List<Plant>>

    @Query("SELECT * FROM PLANT_TABLE WHERE waterDate <= Date('now')")
    fun getPlantsThatNeedWater(): LiveData<List<Plant>>

//    @Query("SELECT * FROM PLANT_TABLE WHERE mist_date <= Date(:dateAsYearMonthDay)")
//    fun getPlantsThatNeedMist(dateAsYearMonthDay: String): LiveData<List<Plant>>

    @Query("SELECT * FROM PLANT_TABLE WHERE mistDate <= Date('now')")
    fun getPlantsThatNeedMist(): LiveData<List<Plant>>

//    @Query("SELECT COUNT(*) FROM PLANT_TABLE WHERE water_date <= Date(:dateAsYearMonthDay)")
//    fun countPlantsThatNeedWater(dateAsYearMonthDay: String): LiveData<Int>

    @Query("SELECT COUNT(*) FROM PLANT_TABLE WHERE waterDate <= Date('now')")
    fun countPlantsThatNeedWater(): LiveData<Int>

//    @Query("SELECT COUNT(*) FROM PLANT_TABLE WHERE mist_date <= Date(:dateAsYearMonthDay)")
//    fun countPlantsThatNeedMist(dateAsYearMonthDay: String): LiveData<Int>

    @Query("SELECT COUNT(*) FROM PLANT_TABLE WHERE mistDate <= Date('now')")
    fun countPlantsThatNeedMist(): LiveData<Int>
}