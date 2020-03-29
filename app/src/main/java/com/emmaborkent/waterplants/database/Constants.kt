package com.emmaborkent.waterplants.database

// Database Constants
const val DATABASE_NAME: String = "com.emmaborkent.waterplants.plants.db"
const val DATABASE_VERSION: Int = 1
const val TABLE_NAME: String = "PLANTS"

// Plant Table Column Names
const val KEY_ID: String = "id"
const val KEY_PLANT_NAME: String = "plant_name"
const val KEY_PLANT_SPECIES: String = "plant_species"
const val KEY_PLANT_IMAGE: String = "plant_image"
const val KEY_PLANT_WATER_DATE: String = "plant_water_date"
const val KEY_PLANT_DAYS_NEXT_WATER: String = "plant_days_to_next_water"
const val KEY_PLANT_MIST_DATE: String = "plant_mist_date"
const val KEY_PLANT_DAYS_NEXT_MIST: String = "plant_days_to_next_mist"