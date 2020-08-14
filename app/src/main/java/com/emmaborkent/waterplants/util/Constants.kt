package com.emmaborkent.waterplants.util

// Database Constants
const val DATABASE_NAME: String = "com.emmaborkent.waterplants.plants.db"

const val DATABASE_TABLE_PLANTS: String = "plant_table"
const val DATABASE_VERSION: Int = 1
const val DATABASE_FILE = "plant_database"

// Plant Table Column Names
const val KEY_ID: String = "id"
const val KEY_PLANT_NAME: String = "plant_name"
const val KEY_PLANT_SPECIES: String = "plant_species"
const val KEY_PLANT_IMAGE: String = "plant_image"
const val KEY_PLANT_WATER_DATE: String = "plant_water_date"
const val KEY_PLANT_WATER_EVERY_DAYS: String = "plant_days_to_next_water"
const val KEY_PLANT_NEEDS_WATER: String = "plant_needs_water"
const val KEY_PLANT_MIST_DATE: String = "plant_mist_date"
const val KEY_PLANT_MIST_EVERY_DAYS: String = "plant_days_to_next_mist"
const val KEY_PLANT_NEEDS_MIST: String = "plant_needs_mist"

// Other Plant Constants
const val PLANT_ID = "userId"

// Tabs
const val KEY_SELECTED_TAB = "selected_tab"

// AddEditActivity
const val PICK_IMAGE_CODE = 1000
const val PERMISSION_CODE = 1001
const val DATE_PICKER = "date_picker"
const val DATE_PICKER_DATE = "date_for_date_picker"
const val DATE_PICKER_BUTTON_WATER = ""
