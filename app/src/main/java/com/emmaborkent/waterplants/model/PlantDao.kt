package com.emmaborkent.waterplants.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
@TypeConverters(DatabaseDateConverter::class)
interface PlantDao {

    @Insert
    suspend fun insert(vararg plant: Plant)

    @Update
    suspend fun update(vararg plant: Plant?)

    @Delete
    suspend fun deletePlant(vararg plant: Plant)

    @Query("SELECT * FROM PLANT_TABLE ORDER BY id DESC LIMIT 1")
    suspend fun getLatestPlant(): Plant?

    @Query("SELECT * FROM PLANT_TABLE WHERE id=:id")
    suspend fun getPlant(id: Int): Plant?

    @Query("SELECT * FROM PLANT_TABLE ORDER BY id DESC")
    fun getAllPlants(): LiveData<List<Plant>>

    @Query("SELECT COUNT(*) FROM PLANT_TABLE")
    suspend fun countAllPlants(): Int

    @Query("SELECT * FROM PLANT_TABLE WHERE waterDate <= Date('now')")
    fun getPlantsThatNeedWater(): LiveData<List<Plant>>

    @Query("SELECT * FROM PLANT_TABLE WHERE mistDate <= Date('now')")
    fun getPlantsThatNeedMist(): LiveData<List<Plant>>

    @Query("SELECT COUNT(*) FROM PLANT_TABLE WHERE waterDate <= Date('now')")
    fun countPlantsThatNeedWater(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM PLANT_TABLE WHERE mistDate <= Date('now')")
    fun countPlantsThatNeedMist(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM PLANT_TABLE WHERE waterDate <= Date('now') OR mistDate <= Date('now')")
    fun countAllPlantsThatNeedWaterOrMist(): LiveData<Int>
}