package com.emmaborkent.waterplants.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.model.PlantEntity
import com.emmaborkent.waterplants.viewmodel.ParseFormatDates

class PlantsTodayAdapter(
    private val clickListener: (PlantEntity) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_WATER = 0
        const val VIEW_TYPE_MIST = 1
    }

    private var plantsThatNeedWater = emptyList<PlantEntity>()
    private var plantsThatNeedMist = emptyList<PlantEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val itemView: View
        return when (viewType) {
            VIEW_TYPE_WATER -> {
                itemView = LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_water_plants, parent, false)
                WaterPlantsHolder(itemView)
            }
            VIEW_TYPE_MIST -> {
                itemView = LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_mist_plants, parent, false)
                MistPlantsHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount() = plantsThatNeedWater.size + plantsThatNeedMist.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WaterPlantsHolder -> holder.bindPlantsWater(plantsThatNeedWater[position], clickListener)
            is MistPlantsHolder -> holder.bindPlantsMist(plantsThatNeedMist[position - plantsThatNeedWater.size], clickListener)
        }
    }

    internal fun setWaterPlants(waterPlants: List<PlantEntity>) {
        this.plantsThatNeedWater = waterPlants
        notifyDataSetChanged()
    }

    internal fun setMistPlants(mistPlants: List<PlantEntity>) {
        this.plantsThatNeedMist = mistPlants
        notifyDataSetChanged()
    }

    inner class WaterPlantsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val plantImage =
            itemView.findViewById<ImageView>(R.id.image_rv_water_plants)
        private val plantName =
            itemView.findViewById<TextView>(R.id.text_rv_water_plants_name)
        private val checkBoxIcon =
            itemView.findViewById<CheckBox>(R.id.toggle_rv_water_plants)
        private val plantDate =
            itemView.findViewById<TextView>(R.id.text_rv_water_plants_date)
        private val todayString = ParseFormatDates()
            .getDefaultDateAsString()

        fun bindPlantsWater(plant: PlantEntity, clickListener: (PlantEntity) -> Unit) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val plantBitmapImage = BitmapFactory.decodeFile(plant.image, bitmapOptions)
            plantImage.setImageBitmap(plantBitmapImage)
            plantName.text = plant.name

            plantDate.text =
                ParseFormatDates().yearMonthDayStringToDayMonthYearString(plant.waterDate)
            if (todayString == plant.waterDate) {
                plantDate.visibility = View.INVISIBLE
            }

            checkBoxIcon.setOnClickListener { waterCheckBox(plant) }
            itemView.setOnClickListener { clickListener(plant) }
        }

        // TODO: 5-6-2020 Check if checked state is correctly recycled or not.
        //  https://blog.oziomaogbe.com/2017/10/18/android-handling-checkbox-state-in-recycler-views.html
        private fun waterCheckBox(plant: PlantEntity) {
            if (checkBoxIcon.isChecked) {
                Log.d("PlantsTodayAdapter", "Plant ${plant.name}: Water Is Checked")
//                Plant().giveWater(plant)
//                dbHandler.updatePlantInDatabase(plant)
            } else {
                Log.d("PlantsTodayAdapter", "Plant ${plant.name}: Water Is NOT Checked")
//                Plant().undoWaterGift(plant)
//                dbHandler.updatePlantInDatabase(plant)
            }
        }
    }

    inner class MistPlantsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val plantImage =
            itemView.findViewById<ImageView>(R.id.image_rv_mist_plants)
        private val plantName =
            itemView.findViewById<TextView>(R.id.text_rv_mist_plants_name)
        private val checkBoxIcon =
            itemView.findViewById<CheckBox>(R.id.toggle_rv_mist_plants)
        private val plantDate =
            itemView.findViewById<TextView>(R.id.text_rv_mist_plants_date)
        private val todayString = ParseFormatDates()
            .getDefaultDateAsString()

        fun bindPlantsMist(plant: PlantEntity, clickListener: (PlantEntity) -> Unit) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val plantBitmapImage = BitmapFactory.decodeFile(plant.image, bitmapOptions)
            plantImage.setImageBitmap(plantBitmapImage)
            plantName.text = plant.name

            plantDate.text =
                ParseFormatDates().yearMonthDayStringToDayMonthYearString(plant.mistDate)
            if (todayString == plant.mistDate) {
                plantDate.visibility = View.INVISIBLE
            }

            checkBoxIcon.setOnClickListener { mistCheckBox(plant) }
            itemView.setOnClickListener { clickListener(plant) }
        }

        private fun mistCheckBox(plant: PlantEntity) {
            if (checkBoxIcon.isChecked) {
                Log.d("PlantsTodayAdapter", "Plant ${plant.name}: Mist Is Checked")
//                Plant().giveMist(plant)
//                dbHandler.updatePlantInDatabase(plant)
            } else {
                Log.d("PlantsTodayAdapter", "Plant ${plant.name}: Mist Is NOT Checked")
//                Plant().undoGiveMist(plant)
//                dbHandler.updatePlantInDatabase(plant)
            }
        }
    }
}