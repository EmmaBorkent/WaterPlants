package com.emmaborkent.waterplants.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.emmaborkent.waterplants.util.*

class XPlantDatabaseHandler// ...
private constructor(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {
    private val classNameTag: String = XPlantDatabaseHandler::class.java.simpleName

    override fun onCreate(db: SQLiteDatabase?) {
        val createPlantsTable = "CREATE TABLE " + DATABASE_TABLE_PLANTS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_PLANT_NAME + " TEXT," +
                KEY_PLANT_SPECIES + " TEXT," +
                KEY_PLANT_IMAGE + " TEXT," +
                KEY_PLANT_WATER_DATE + " TEXT," +
                KEY_PLANT_WATER_EVERY_DAYS + " INT," +
                KEY_PLANT_NEEDS_WATER + " BOOLEAN NOT NULL CHECK (" + KEY_PLANT_NEEDS_WATER + " IN (0,1))," +
                KEY_PLANT_MIST_DATE + " TEXT," +
                KEY_PLANT_MIST_EVERY_DAYS + " INT," +
                KEY_PLANT_NEEDS_MIST + " BOOLEAN NOT NULL CHECK (" + KEY_PLANT_NEEDS_MIST + " IN (0,1)))"

        db?.execSQL(createPlantsTable)
        Log.d(classNameTag, "Database Table Created")
    }

    // TODO: 26-4-2020 change onUpgrade so that it does not lose all data every time
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $DATABASE_TABLE_PLANTS")
        onCreate(db)
        Log.d(classNameTag, "Database Upgraded")
    }

    companion object {
        private var instance: XPlantDatabaseHandler? = null
        fun getInstance(context: Context): XPlantDatabaseHandler {
            if (instance == null) {
                instance = XPlantDatabaseHandler(context)
            }
            return instance!!
        }
    }

    // TODO: 13-4-2020 improve database functions

    fun savePlantToDatabase(plant: XPlant) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_PLANT_NAME, plant.name)
        values.put(KEY_PLANT_SPECIES, plant.species)
        values.put(KEY_PLANT_IMAGE, plant.image)
        values.put(KEY_PLANT_WATER_DATE, plant.datePlantNeedsWater)
        values.put(KEY_PLANT_WATER_EVERY_DAYS, plant.daysToNextWater)
        values.put(KEY_PLANT_NEEDS_WATER, plant.needsWater)
        values.put(KEY_PLANT_MIST_DATE, plant.datePlantNeedsMist)
        values.put(KEY_PLANT_MIST_EVERY_DAYS, plant.daysToNextMist)
        values.put(KEY_PLANT_NEEDS_MIST, plant.needsMist)

        db.insert(DATABASE_TABLE_PLANTS, null, values)
        db.close()

        Log.d(classNameTag, "New plant is added to database, date: ${plant.datePlantNeedsWater}")
    }

    private fun intToBoolean(int: Int): Boolean {
        return int == 1
    }

    fun readPlant(id: Int): XPlant {
        val db = readableDatabase
        val cursor = db.query(
            DATABASE_TABLE_PLANTS,
            arrayOf(
                KEY_ID,
                KEY_PLANT_NAME,
                KEY_PLANT_SPECIES,
                KEY_PLANT_IMAGE,
                KEY_PLANT_WATER_DATE,
                KEY_PLANT_WATER_EVERY_DAYS,
                KEY_PLANT_NEEDS_WATER,
                KEY_PLANT_MIST_DATE,
                KEY_PLANT_MIST_EVERY_DAYS,
                KEY_PLANT_NEEDS_MIST
            ), "$KEY_ID=?", arrayOf(id.toString()),
            null, null, null, null
        )

        cursor?.moveToFirst()
        val plant = newPlantInstance(cursor)
        cursor.close()
        Log.d(classNameTag, "Reading Plant ${plant.id} from Database")
        return plant
    }

    fun readAllPlants(): ArrayList<XPlant> {
        val db = readableDatabase
        val allPlants: ArrayList<XPlant> = ArrayList()
        val queryAllPlants = "SELECT * FROM $DATABASE_TABLE_PLANTS"
        val cursor = db.rawQuery(queryAllPlants, null)

        if (cursor.moveToFirst()) {
            do {
                val plant = newPlantInstance(cursor)
                allPlants.add(plant)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return allPlants
    }

    fun findPlantsOnDay(date: String): ArrayList<XPlant> {
        return getPlantsThatNeedWater(date)
    }

    // TODO: 13-4-2020 Why has this function a return value? 
//    fun updatePlantInDatabase(plant: Plant): Int {
//        val db = writableDatabase
//        val values = ContentValues()
//        values.put(KEY_PLANT_NAME, plant.name)
//        values.put(KEY_PLANT_SPECIES, plant.species)
//        values.put(KEY_PLANT_IMAGE, plant.image)
//        values.put(KEY_PLANT_WATER_DATE, plant.datePlantNeedsWater)
//        values.put(KEY_PLANT_DAYS_NEXT_WATER, plant.daysToNextWater)
//        values.put(KEY_PLANT_NEEDS_WATER, plant.needsWater)
//        values.put(KEY_PLANT_MIST_DATE, plant.datePlantNeedsMist)
//        values.put(KEY_PLANT_DAYS_NEXT_MIST, plant.daysToNextMist)
//        values.put(KEY_PLANT_NEEDS_MIST, plant.needsMist)
//
//        Log.d(classNameTag, "Plant ${plant.id} is Updated")
//        return db.update(TABLE_NAME, values, "$KEY_ID=?", arrayOf(plant.id.toString()))
//    }

    fun updatePlantInDatabase(plant: XPlant) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_PLANT_NAME, plant.name)
        values.put(KEY_PLANT_SPECIES, plant.species)
        values.put(KEY_PLANT_IMAGE, plant.image)
        values.put(KEY_PLANT_WATER_DATE, plant.datePlantNeedsWater)
        values.put(KEY_PLANT_WATER_EVERY_DAYS, plant.daysToNextWater)
        values.put(KEY_PLANT_NEEDS_WATER, plant.needsWater)
        values.put(KEY_PLANT_MIST_DATE, plant.datePlantNeedsMist)
        values.put(KEY_PLANT_MIST_EVERY_DAYS, plant.daysToNextMist)
        values.put(KEY_PLANT_NEEDS_MIST, plant.needsMist)

        db.update(DATABASE_TABLE_PLANTS, values, "$KEY_ID=?", arrayOf(plant.id.toString()))
        db.close()

        Log.d(classNameTag, "Plant ${plant.id} is Updated")
    }

    fun deletePlant(id: Int) {
        val db = writableDatabase
        db.delete(DATABASE_TABLE_PLANTS, "$KEY_ID=?", arrayOf(id.toString()))
        db.close()

        Log.d(classNameTag, "Plant is Deleted")
    }

    fun deleteAllPlants() {
        val db = writableDatabase
        db.delete(DATABASE_TABLE_PLANTS, null, null)
        db.close()

        Log.d(classNameTag, "All plants are deleted")
    }

    fun countAllPlants(): Int {
        val db = readableDatabase
        val countQuery = "SELECT * FROM $DATABASE_TABLE_PLANTS"
        val cursor = db.rawQuery(countQuery, null)

        Log.d(classNameTag, "There are ${cursor.count} plants in Database")
        cursor.close()
        return cursor.count
    }

    fun printAllPlantIds() {
        val db = readableDatabase
        val queryAllPlants = "SELECT * FROM $DATABASE_TABLE_PLANTS"
        val cursor = db.rawQuery(queryAllPlants, null)

        if (cursor.moveToFirst()) {
            do {
                Log.d(classNameTag, "Plant ID: ${cursor.getInt(cursor.getColumnIndex(
                    KEY_ID
                ))}")
            } while (cursor.moveToNext())
        }

        cursor.close()
    }

    fun printAllPlantWaterDates() {
        val db = readableDatabase
        val queryAllPlants = "SELECT * FROM $DATABASE_TABLE_PLANTS"
        val cursor = db.rawQuery(queryAllPlants, null)
        if (cursor.moveToFirst()) {
            do {
                Log.d(
                    classNameTag,
                    "Plant ${cursor.getString(cursor.getColumnIndex(KEY_PLANT_NAME))}: " +
                            cursor.getString(cursor.getColumnIndex(KEY_PLANT_WATER_DATE))
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
    }

    fun printAllPlantsThatNeedWater() {
        val db = readableDatabase
        val queryAllPlants = "SELECT * FROM $DATABASE_TABLE_PLANTS"
        val cursor = db.rawQuery(queryAllPlants, null)
        if (cursor.moveToFirst()) {
            do {
                Log.d(
                    classNameTag,
                    "${cursor.getString(cursor.getColumnIndex(KEY_PLANT_NAME))} needs water? " +
                            intToBoolean(cursor.getInt(cursor.getColumnIndex(KEY_PLANT_NEEDS_WATER)))
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    fun countPlantsOnDay(dateAsYearMonthDay: String): Int {
        return getPlantsThatNeedWater(dateAsYearMonthDay).count()
    }

    fun getPlantsThatNeedWater(dateAsYearMonthDay: String): ArrayList<XPlant> {
        val db = readableDatabase
        val plantsThatNeedWater: ArrayList<XPlant> = ArrayList()
        val queryPlantsThatNeedWater =
            "SELECT * FROM $DATABASE_TABLE_PLANTS WHERE $KEY_PLANT_WATER_DATE <= Date('$dateAsYearMonthDay')"
        val cursor = db.rawQuery(queryPlantsThatNeedWater, null)
        if (cursor.moveToFirst()) {
            do {
                val plant = newPlantInstance(cursor)
                plantsThatNeedWater.add(plant)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return plantsThatNeedWater
    }

    fun getPlantsThatNeedMist(dateAsYearMonthDate: String): ArrayList<XPlant> {
        val db = readableDatabase
        val plantsThatNeedMist: ArrayList<XPlant> = ArrayList()
        val queryPlantsThatNeedMist =
            "SELECT * FROM $DATABASE_TABLE_PLANTS WHERE $KEY_PLANT_MIST_DATE <= Date('$dateAsYearMonthDate')"
        val cursor = db.rawQuery(queryPlantsThatNeedMist, null)
        if (cursor.moveToFirst()) {
            do {
                val plant = newPlantInstance(cursor)
                plantsThatNeedMist.add(plant)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return plantsThatNeedMist
    }

    private fun newPlantInstance(cursor: Cursor): XPlant {
        val plant = XPlant()
        plant.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
        plant.name = cursor.getString(cursor.getColumnIndex(KEY_PLANT_NAME))
        plant.species = cursor.getString(cursor.getColumnIndex(KEY_PLANT_SPECIES))
        plant.image = cursor.getString(cursor.getColumnIndex(KEY_PLANT_IMAGE))
        plant.datePlantNeedsWater = cursor.getString(cursor.getColumnIndex(KEY_PLANT_WATER_DATE))
        plant.daysToNextWater = cursor.getInt(cursor.getColumnIndex(KEY_PLANT_WATER_EVERY_DAYS))
        plant.needsWater = intToBoolean(cursor.getInt(cursor.getColumnIndex(KEY_PLANT_NEEDS_WATER)))
        plant.datePlantNeedsMist = cursor.getString(cursor.getColumnIndex(KEY_PLANT_MIST_DATE))
        plant.daysToNextMist = cursor.getInt(cursor.getColumnIndex(KEY_PLANT_MIST_EVERY_DAYS))
        plant.needsMist = intToBoolean(cursor.getInt(cursor.getColumnIndex(KEY_PLANT_NEEDS_MIST)))
        return plant
    }
}