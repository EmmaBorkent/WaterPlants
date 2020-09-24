package com.emmaborkent.waterplants

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.model.PlantDao
import com.emmaborkent.waterplants.model.PlantDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class PlantDatabaseTest {

    private lateinit var plantDao: PlantDao
    private lateinit var db: PlantDatabase
    private val scope = CoroutineScope(Dispatchers.IO)

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, PlantDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        plantDao = db.plantDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndGetLatestPlant() {
        val plant = Plant()
        plantDao.insert(plant)
        val latestPlant = plantDao.getLatestPlant()
        Assert.assertEquals(latestPlant?.waterEveryDays, 1)
        Assert.assertEquals(latestPlant?.id, 1)
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndGetById() {
        val plant = Plant()
        plantDao.insert(plant)
        val getPlantById = plantDao.getPlant(1)
        Assert.assertEquals(getPlantById?.id, 1)
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertTwoGetLatest() {
        val plant1 = Plant()
        val plant2 = Plant()
        plantDao.apply {
            insert(plant1)
            insert(plant2)
        }
        val getLatest = plantDao.getLatestPlant()
        Assert.assertEquals(getLatest?.id, 2)
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndUpdate() {
        val newPlant = Plant()
        plantDao.insert(newPlant)
        val getPlant = plantDao.getLatestPlant()
        Assert.assertEquals(getPlant?.waterEveryDays, 1)
        getPlant?.waterEveryDays = 3
        if (getPlant != null) {
            plantDao.update(getPlant)
        }
        val getUpdatedPlant = plantDao.getLatestPlant()
        Assert.assertEquals(getUpdatedPlant?.waterEveryDays, 3)
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndDelete() {
        val plant = Plant()
        plantDao.insert(plant)
        val plantFromDb = plantDao.getLatestPlant()
        if (plantFromDb != null) {
            plantDao.deletePlant(plantFromDb)
        }
        assertNull(plantFromDb)
    }

    @Test
    @Throws(Exception::class)
    suspend fun deleteUserTest() {
        val plant = Plant()
        plantDao.insert(plant)
        var plantFromDb = plantDao.getLatestPlant()
        if (plantFromDb != null) {
            plantDao.deletePlant(plantFromDb)
        }
        plantFromDb = plantDao.getLatestPlant()
        assertNull(plantFromDb)
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndCountAllPlants() {
        val plant1 = Plant()
        val plant2 = Plant()
        plantDao.apply {
            insert(plant1)
            insert(plant2)
        }
        val allPlantsCount = plantDao.countAllPlants()
        Assert.assertEquals(allPlantsCount, 2)
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndGetAllPlants() {
        val plant1 = Plant()
        val plant2 = Plant()
        plantDao.apply {
            insert(plant1)
            insert(plant2)
        }
        val allPlantsCount = plantDao.countAllPlants()
        Assert.assertEquals(allPlantsCount, 2)
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Test
    @Throws(Exception::class)
    fun getAllPlantsAsLiveDataTest() {
        val plant1 = Plant()
        val plant2 = Plant()
        scope.launch {
            plantDao.apply {
                insert(plant1)
                insert(plant2)
            }
        }
        val allPlantsWrappedNLiveData = plantDao.getAllPlants()
        val plantFromDb = allPlantsWrappedNLiveData.getValueBlocking()
        val size = plantFromDb?.size
        assertEquals(size, 2)
    }

    @Test
    @Throws(Exception::class)
    fun getAndCountPlantsThatNeedWaterLiveData() {
        val plant1 = Plant()
        plant1.waterDate = plant1.waterDate.plusDays(1)
        val plant2 = Plant()
        scope.launch {
            plantDao.apply {
                insert(plant1)
                insert(plant2)
            }
        }
        val getAllPlantsThatNeedWaterLiveData = plantDao.getPlantsThatNeedWater()
        val getAllPlantsThatNeedWaterList = getAllPlantsThatNeedWaterLiveData.getValueBlocking()
        val plantsThatNeedWater = getAllPlantsThatNeedWaterList?.size
        Assert.assertEquals(plantsThatNeedWater, 1)

        scope.launch {
            val countAllPlants = plantDao.countAllPlants()
            Assert.assertEquals(countAllPlants, 2)
        }

        val countPlantsThatNeedWaterLiveData = plantDao.countPlantsThatNeedWater()
        val countPlantsThatNeedWater = countPlantsThatNeedWaterLiveData.getValueBlocking()
        Assert.assertEquals(countPlantsThatNeedWater, 1)
    }

    @Test
    @Throws(Exception::class)
    fun getAndCountPlantsThatNeedMistLiveData() {
        val plant1 = Plant()
        plant1.mistDate = plant1.mistDate.plusDays(1)
        val plant2 = Plant()
        scope.launch {
            plantDao.apply {
                insert(plant1)
                insert(plant2)
            }
        }
        val getAllPlantsThatNeedMistLiveData = plantDao.getPlantsThatNeedMist()
        val getAllPlantsThatNeedMistList = getAllPlantsThatNeedMistLiveData.getValueBlocking()
        val plantsThatNeedMist = getAllPlantsThatNeedMistList?.size
        Assert.assertEquals(plantsThatNeedMist, 1)

        scope.launch {
            val countAllPlants = plantDao.countAllPlants()
            Assert.assertEquals(countAllPlants, 2)
        }

        val countPlantsThatNeedMistLiveData = plantDao.countPlantsThatNeedMist()
        val countPlantsThatNeedMist = countPlantsThatNeedMistLiveData.getValueBlocking()
        Assert.assertEquals(countPlantsThatNeedMist, 1)
    }

    @Throws(InterruptedException::class)
    fun <T> LiveData<T>.getValueBlocking(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val innerObserver = Observer<T> {
            value = it
            latch.countDown()
        }
        observeForever(innerObserver)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }

}