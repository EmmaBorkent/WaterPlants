package com.emmaborkent.waterplants.ui

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.database.Plant

class WaterPlantsRecyclerAdapter(
    private val plantsList: ArrayList<Plant>,
    private val context: Context,
    private val clickListener: (Plant) -> Unit
) : RecyclerView.Adapter<WaterPlantsRecyclerAdapter.PlantsHolder>() {

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
        private val plantImage = itemView
            .findViewById<ImageView>(R.id.image_rv_water_plants)
        private val plantName = itemView
            .findViewById<TextView>(R.id.text_rv_water_plants_name)
        private val plantSpecies =
            itemView.findViewById<TextView>(R.id.text_rv_water_plants_species)

        fun bindPlants(plant: Plant, clickListener: (Plant) -> Unit) {
            plantImage.setImageURI(Uri.parse(plant.image))
            plantName.text = plant.name
            plantSpecies.text = plant.species
            itemView.setOnClickListener { clickListener(plant) }
        }
    }
}