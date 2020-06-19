package com.emmaborkent.waterplants.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Plant::class], version = DATABASE_VERSION)
abstract class PlantDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao

    companion object {
        private var INSTANCE: PlantDatabase? = null

        fun getDatabaseInstance(context: Context): PlantDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // TODO: 5-6-2020 Migrate your database functions on newer versions with
            //  https://developer.android.com/training/data-storage/room/migrating-db-versions
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantDatabase::class.java,
                    DATABASE_FILE
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}