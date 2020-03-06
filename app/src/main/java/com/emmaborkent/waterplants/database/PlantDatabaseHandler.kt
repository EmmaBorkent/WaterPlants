package com.emmaborkent.waterplants.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.emmaborkent.waterplants.*

class PlantDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createPlantsTable = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_PLANT_NAME + " TEXT," +
                KEY_PLANT_SPECIES + " TEXT," +
                KEY_PLANT_IMAGE + " TEXT," +
                KEY_PLANT_END_DATE + " LONG," +
                KEY_PLANT_REPEAT + " LONG" + ")"

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
        values.put(KEY_PLANT_END_DATE, plant.endDate)
        values.put(KEY_PLANT_REPEAT, plant.repeat)

        db.insert(TABLE_NAME, null, values)
        db.close()

        Log.d("PlantDatabaseHandler", "Created New Plant")
    }

    fun readPlant(id: Long): Plant {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME,
            arrayOf(
                KEY_ID,
                KEY_PLANT_NAME,
                KEY_PLANT_SPECIES,
                KEY_PLANT_IMAGE,
                KEY_PLANT_END_DATE,
                KEY_PLANT_REPEAT), "$KEY_ID=?", arrayOf(id.toString()),
            null, null, null, null
        )

        cursor?.moveToFirst()
        val plant = Plant()
        plant.name = cursor.getString(cursor.getColumnIndex(KEY_PLANT_NAME))
        plant.species = cursor.getString(cursor.getColumnIndex(KEY_PLANT_SPECIES))
        plant.image = cursor.getString(cursor.getColumnIndex(KEY_PLANT_IMAGE))
        plant.endDate = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_END_DATE))
        plant.repeat = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_REPEAT))

        cursor.close()
        Log.d("PlantDatabaseHandler", "Reading a Plant from Database")
        return plant
    }

    fun readAllPlants(): ArrayList<Plant> {
        val db = readableDatabase
        val list: ArrayList<Plant> = ArrayList()
        val selectAll = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectAll, null)

        if (cursor.moveToFirst()) {
            do {
                val plant = Plant()
                plant.name = cursor.getString(cursor.getColumnIndex(KEY_PLANT_NAME))
                plant.species = cursor.getString(cursor.getColumnIndex(KEY_PLANT_SPECIES))
                plant.image = cursor.getString(cursor.getColumnIndex(KEY_PLANT_IMAGE))
                plant.endDate = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_END_DATE))
                plant.repeat = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_REPEAT))

                list.add(plant)
            } while (cursor.moveToNext())
        }

        cursor.close()
        Log.d("PlantDatabaseHandler", "Reading All Plants from Database")
        return list
    }

    fun findDay(fromTime: Long, toTime: Long): ArrayList<Plant> {
        val db = readableDatabase
        val list: ArrayList<Plant> = ArrayList()
        val query = "SELECT * FROM $TABLE_NAME WHERE $KEY_PLANT_END_DATE >= \"$toTime\" " +
                "AND $KEY_PLANT_END_DATE <= \"$fromTime\""

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val plant = Plant()
                plant.name = cursor.getString(cursor.getColumnIndex(KEY_PLANT_NAME))
                plant.species = cursor.getString(cursor.getColumnIndex(KEY_PLANT_SPECIES))
                plant.image = cursor.getString(cursor.getColumnIndex(KEY_PLANT_IMAGE))
                plant.endDate = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_END_DATE))
                plant.repeat = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_REPEAT))

                list.add(plant)
            } while (cursor.moveToNext())
        }

        cursor.close()
        Log.d("PlantDatabaseHandler",
            "Reading Drinks from Database between $fromTime and $toTime")
        return list
    }

    fun updatePlant(plant: Plant): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_PLANT_NAME, plant.name)
        values.put(KEY_PLANT_SPECIES, plant.species)
        values.put(KEY_PLANT_IMAGE, plant.image)
        values.put(KEY_PLANT_END_DATE, plant.endDate)
        values.put(KEY_PLANT_REPEAT, plant.repeat)

        Log.d("PlantDatabaseHandler", "Plant is Updated")
        return db.update(TABLE_NAME, values, "$KEY_ID=?", arrayOf(plant.id.toString()))
    }

    fun deletePlant(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$KEY_ID=?", arrayOf(id.toString()))
        db.close()

        Log.d("PlantDatabaseHandler", "Plant is Deleted")
    }

    fun getCount(): Int {
        val db = readableDatabase
        val countQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(countQuery, null)

        Log.d("PlantDatabaseHandler", "There are ${cursor.count} plants in Database")
        cursor.close()
        return cursor.count
    }

    fun getCountOfDay(fromTime: Long, toTime: Long): Int {
        val db = readableDatabase
        val list: ArrayList<Plant> = ArrayList()
        val countQueryOfDay = "SELECT * FROM $TABLE_NAME WHERE $KEY_PLANT_END_DATE >= " +
                "\"$toTime\" AND $KEY_PLANT_END_DATE <= \"$fromTime\""
        val cursor = db.rawQuery(countQueryOfDay, null)

        if (cursor.moveToFirst()) {
            do {
                val plant = Plant()
                plant.name = cursor.getString(cursor.getColumnIndex(KEY_PLANT_NAME))
                plant.species = cursor.getString(cursor.getColumnIndex(KEY_PLANT_SPECIES))
                plant.image = cursor.getString(cursor.getColumnIndex(KEY_PLANT_IMAGE))
                plant.endDate = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_END_DATE))
                plant.repeat = cursor.getLong(cursor.getColumnIndex(KEY_PLANT_REPEAT))

                list.add(plant)
            } while (cursor.moveToNext())
        }

        cursor.close()
        Log.d("PlantDatabaseHandler", "There are ${list.count()} plants to Water " +
                "between $fromTime and $toTime")
        return list.count()
    }
}