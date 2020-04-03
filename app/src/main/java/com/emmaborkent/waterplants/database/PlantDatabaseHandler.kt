package com.emmaborkent.waterplants.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class PlantDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createPlantsTable = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_PLANT_NAME + " TEXT," +
                KEY_PLANT_SPECIES + " TEXT," +
                KEY_PLANT_IMAGE + " TEXT," +
                KEY_PLANT_WATER_DATE + " LONG," +
                KEY_PLANT_DAYS_NEXT_WATER + " LONG," +
                KEY_PLANT_MIST_DATE + " LONG," +
                KEY_PLANT_DAYS_NEXT_MIST + " LONG" + ")"

        db?.execSQL(createPlantsTable)
        Log.d("PlantDatabaseHandler", "Database Table Created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
        Log.d("PlantDatabaseHandler", "Database Upgraded")
    }

    // CRUD - Create, Read, Update, Delete

    fun createPlant(plant: Plant) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_PLANT_NAME, plant.name)
        values.put(KEY_PLANT_SPECIES, plant.species)
        values.put(KEY_PLANT_IMAGE, plant.image)
        values.put(KEY_PLANT_WATER_DATE, plant.datePlantNeedsWater)
        values.put(KEY_PLANT_DAYS_NEXT_WATER, plant.daysToNextWater)
        values.put(KEY_PLANT_MIST_DATE, plant.datePlantNeedsMist)
        values.put(KEY_PLANT_DAYS_NEXT_MIST, plant.daysToNextMist)

        db.insert(TABLE_NAME, null, values)
        db.close()

        Log.d("PlantDatabaseHandler", "Created New Plant")
    }

    private fun newPlantInstance(cursor: Cursor): Plant {
        val plant = Plant()
        plant.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
        plant.name = cursor.getString(cursor.getColumnIndex(KEY_PLANT_NAME))
        plant.species = cursor.getString(cursor.getColumnIndex(KEY_PLANT_SPECIES))
        plant.image = cursor.getString(cursor.getColumnIndex(KEY_PLANT_IMAGE))
        plant.datePlantNeedsWater = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_WATER_DATE))
        plant.daysToNextWater = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_DAYS_NEXT_WATER))
        plant.datePlantNeedsMist = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_MIST_DATE))
        plant.daysToNextMist = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_DAYS_NEXT_MIST))
        return plant
    }

    fun readPlant(id: Int): Plant {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(
                KEY_ID,
                KEY_PLANT_NAME,
                KEY_PLANT_SPECIES,
                KEY_PLANT_IMAGE,
                KEY_PLANT_WATER_DATE,
                KEY_PLANT_DAYS_NEXT_WATER,
                KEY_PLANT_MIST_DATE,
                KEY_PLANT_DAYS_NEXT_MIST
            ), "$KEY_ID=?", arrayOf(id.toString()),
            null, null, null, null
        )

        cursor?.moveToFirst()
        val plant = newPlantInstance(cursor)
        cursor.close()
        Log.d("PlantDatabaseHandler", "Reading a Plant from Database")
        return plant
    }

    fun readAllPlants(): ArrayList<Plant> {
        val db = readableDatabase
        val allPlants: ArrayList<Plant> = ArrayList()
        val queryAllPlants = "SELECT * FROM $TABLE_NAME"
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

    fun findPlantsOnDay(startTime: Long, endTime: Long): ArrayList<Plant> {
        return getPlantsOnDay(startTime, endTime)
    }

    fun updatePlant(plant: Plant): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_PLANT_NAME, plant.name)
        values.put(KEY_PLANT_SPECIES, plant.species)
        values.put(KEY_PLANT_IMAGE, plant.image)
        values.put(KEY_PLANT_WATER_DATE, plant.datePlantNeedsWater)
        values.put(KEY_PLANT_DAYS_NEXT_WATER, plant.daysToNextWater)
        values.put(KEY_PLANT_MIST_DATE, plant.datePlantNeedsMist)
        values.put(KEY_PLANT_DAYS_NEXT_MIST, plant.daysToNextMist)

        Log.d("PlantDatabaseHandler", "Plant is Updated")
        return db.update(TABLE_NAME, values, "$KEY_ID=?", arrayOf(plant.id.toString()))
    }

    fun deletePlant(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$KEY_ID=?", arrayOf(id.toString()))
        db.close()

        Log.d("PlantDatabaseHandler", "Plant is Deleted")
    }

    fun countAllPlants(): Int {
        val db = readableDatabase
        val countQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(countQuery, null)

        Log.d("PlantDatabaseHandler", "There are ${cursor.count} plants in Database")
        cursor.close()
        return cursor.count
    }

    fun printAllPlantIds() {
        val db = readableDatabase
        val queryAllPlants = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(queryAllPlants, null)

        if (cursor.moveToFirst()) {
            do {
                Log.d("PlantDatabaseHandler",
                    "Plant ID: ${cursor.getInt(cursor.getColumnIndex(KEY_ID))}")
            } while (cursor.moveToNext())
        }

        cursor.close()
    }

    fun countPlantsOnDay(startTime: Long, endTime: Long): Int {
        return getPlantsOnDay(startTime, endTime).count()
    }

    private fun getPlantsOnDay(startTime: Long, endTime: Long): ArrayList<Plant> {
        val db = readableDatabase
        val allPlantsOnDay: ArrayList<Plant> = ArrayList()
        val queryPlantsOnDay = "SELECT * FROM $TABLE_NAME WHERE $KEY_PLANT_WATER_DATE >= " +
                "\"$endTime\" AND $KEY_PLANT_WATER_DATE <= \"$startTime\""
        val cursor = db.rawQuery(queryPlantsOnDay, null)

        if (cursor.moveToFirst()) {
            do {
                val plant = newPlantInstance(cursor)
                allPlantsOnDay.add(plant)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return allPlantsOnDay
    }
}