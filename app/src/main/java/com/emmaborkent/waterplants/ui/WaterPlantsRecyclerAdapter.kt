package com.emmaborkent.waterplants.ui

import android.content.Context
import android.net.Uri
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

class WaterPlantsRecyclerAdapter(private val plantsList: ArrayList<Plant>,
                                 private val context: Context,
                                 private val clickListener: (Plant) -> Unit)
    : RecyclerView.Adapter<WaterPlantsRecyclerAdapter.PlantsHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WaterPlantsRecyclerAdapter.PlantsHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_water_plants,
            parent, false)
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
        private val plantSpecies =
            itemView.findViewById<TextView>(R.id.text_rv_waterplants_species)
        private val plantIconWaterOrMist =
            itemView.findViewById<CheckBox>(R.id.togglebutton_rv_waterplants)
        private val plantDate =
            itemView.findViewById<TextView>(R.id.text_rv_waterplants_date)

        fun bindPlants(plant: Plant, clickListener: (Plant) -> Unit) {
            plantImage.setImageURI(Uri.parse(plant.image))
            plantName.text = plant.name
            plantSpecies.text = plant.species
            if (plant.needsWater) {
                plantIconWaterOrMist.buttonDrawable =
                    context.resources.getDrawable(R.drawable.ic_toggle_water_background, null)
                // TODO 07-05-20 change so that date is only visible if it is not today
                plantDate.text = ParseFormatDates().yearMonthDayStringToDayMonthYearString(plant.datePlantNeedsWater)
            }
            if (plant.needsMist) {
                plantIconWaterOrMist.buttonDrawable =
                    context.resources.getDrawable(R.drawable.ic_toggle_mist_background, null)
                plantDate.text = ParseFormatDates().yearMonthDayStringToDayMonthYearString(plant.datePlantNeedsMist)
            }
            itemView.setOnClickListener { clickListener(plant) }
        }
    }
}