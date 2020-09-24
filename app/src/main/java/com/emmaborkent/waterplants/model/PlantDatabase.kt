package com.emmaborkent.waterplants.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emmaborkent.waterplants.util.DATABASE_FILE
import com.emmaborkent.waterplants.util.DATABASE_VERSION

// Whenever you change the schema, you'll have to increase the version number for live projects
// For development you need to delete the existing data in the app on the emulator or device
@Database(entities = [Plant::class], version = DATABASE_VERSION, exportSchema = false)
abstract class PlantDatabase : RoomDatabase() {

    abstract val plantDao: PlantDao

//    abstract fun plantDao(): PlantDao

    companion object {
        // The value of a volatile variable will never be cached, and all writes and reads will be
        // done to and from the main memory. This helps make sure the value of INSTANCE is always
        // up-to-date and the same to all execution threads. It means that changes made by one
        // thread to INSTANCE are visible to all other threads immediately, and you don't get a
        // situation where, say, two threads each update the same entity in a cache, which would
        // create a problem.
        @Volatile
        private var INSTANCE: PlantDatabase? = null

        fun getDatabaseInstance(context: Context): PlantDatabase {

            // TODO: 5-6-2020 Migrate your database functions on newer versions with
            //  https://developer.android.com/training/data-storage/room/migrating-db-versions
            // Multiple threads can potentially ask for a database instance at the same time,
            // resulting in two databases instead of one. It's possible for more complex apps.
            // Wrapping the code to get the database into synchronized means that only one thread of
            // execution at a time can enter this block of code, which makes sure the database only
            // gets initialized once.

            synchronized(this) {

                // This is to take advantage of smart cast, which is only available to local
                // variables.
                var instance = INSTANCE

                // Migrations: https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PlantDatabase::class.java,
                        DATABASE_FILE
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}