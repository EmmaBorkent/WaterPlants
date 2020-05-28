package com.emmaborkent.waterplants.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.ParseFormatDates
import com.emmaborkent.waterplants.database.Plant
import com.emmaborkent.waterplants.database.PlantDatabaseHandler

class WaterPlantsRecyclerAdapter(
    private val plantsList: ArrayList<Plant>,
    private val context: Context,
    private val clickListener: (Plant) -> Unit
) : RecyclerView.Adapter<WaterPlantsRecyclerAdapter.PlantsHolder>() {
//    private val classNameTag: String = WaterPlantsRecyclerAdapter::class.java.simpleName
    val dbHandler = PlantDatabaseHandler.getInstance(context)
    private val classNameTag: String = AddEditPlantActivity::class.java.simpleName

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WaterPlantsRecyclerAdapter.PlantsHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.recycler_view_water_plants,
            parent, false
        )
        return PlantsHolder(view)
    }

    override fun getItemCount(): Int {
        return plantsList.size
    }

    override fun onBindViewHolder(holder: WaterPlantsRecyclerAdapter.PlantsHolder, position: Int) {
        holder.bindPlants(plantsList[position], clickListener)
    }

    inner class PlantsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val plantImage =
            itemView.findViewById<ImageView>(R.id.image_rv_waterplants)
        private val plantName =
            itemView.findViewById<TextView>(R.id.text_rv_waterplants_name)
        private val checkBoxIcon =
            itemView.findViewById<CheckBox>(R.id.toggle_rv_waterplants)
        private val plantDate =
            itemView.findViewById<TextView>(R.id.text_rv_waterplants_date)
        private val todayString = ParseFormatDates().getDefaultDateAsString()

        // TODO: 8-5-2020 Shorten function bindPlants
        fun bindPlants(plant: Plant, clickListener: (Plant) -> Unit) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val plantBitmapImage = BitmapFactory.decodeFile(plant.image, bitmapOptions)
            plantImage.setImageBitmap(plantBitmapImage)
            plantName.text = plant.name
            bindOtherViewsForWaterOrMist(plant)
            itemView.setOnClickListener { clickListener(plant) }
        }

        private fun bindOtherViewsForWaterOrMist(plant: Plant) {
            if (plant.needsWater) {
                bindViewsForWater(plant)
            }
            if (plant.needsMist) {
                bindViewsForMist(plant)
            }
        }

        private fun bindViewsForWater(plant: Plant) {
            setWaterIcon()
            setWaterDate(plant)
            checkBoxIcon.setOnClickListener { waterCheckBox(plant) }
        }

        private fun bindViewsForMist(plant: Plant) {
            setMistIcon()
            setMistDate(plant)
            checkBoxIcon.setOnClickListener { mistCheckBox(plant) }
        }

        private fun setWaterIcon() {
            checkBoxIcon.buttonDrawable =
                context.resources.getDrawable(R.drawable.toggle_water_rv, null)
        }

        private fun setMistIcon() {
            checkBoxIcon.buttonDrawable =
                context.resources.getDrawable(R.drawable.ic_toggle_mist_background_icon, null)
        }

        private fun setWaterDate(plant: Plant) {
            plantDate.text =
                ParseFormatDates().yearMonthDayStringToDayMonthYearString(plant.datePlantNeedsWater)
            if (todayString == plant.datePlantNeedsWater) {
                plantDate.visibility = View.INVISIBLE
            }
        }

        private fun setMistDate(plant: Plant) {
            plantDate.text =
                ParseFormatDates().yearMonthDayStringToDayMonthYearString(plant.datePlantNeedsMist)
            if (todayString == plant.datePlantNeedsMist) {
                plantDate.visibility = View.INVISIBLE
            }
        }

        private fun waterCheckBox(plant: Plant) {
            if (checkBoxIcon.isChecked) {
                Plant().giveWater(plant)
                dbHandler.updatePlantInDatabase(plant)
            } else {
                Plant().undoWaterGift(plant)
                dbHandler.updatePlantInDatabase(plant)
            }
        }

        private fun mistCheckBox(plant: Plant) {
            if (checkBoxIcon.isChecked) {
                Plant().giveMist(plant)
                dbHandler.updatePlantInDatabase(plant)
            } else {
                Plant().undoGiveMist(plant)
                dbHandler.updatePlantInDatabase(plant)
            }
        }
    }
}