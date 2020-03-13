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

class AllPlantsRecyclerAdapter(private val plantsList: ArrayList<Plant>, private val context: Context)
    : RecyclerView.Adapter<AllPlantsRecyclerAdapter.PlantsHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllPlantsRecyclerAdapter.PlantsHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_all_plants,
            parent, false)
        return PlantsHolder(view)
    }

    override fun getItemCount(): Int {
        return plantsList.size
    }

    override fun onBindViewHolder(holder: AllPlantsRecyclerAdapter.PlantsHolder, position: Int) {
        holder.bindPlants(plantsList[position])
    }

    inner class PlantsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val plantImage = itemView
            .findViewById<ImageView>(R.id.rv_all_plants_image)
        private val plantName = itemView
            .findViewById<TextView>(R.id.rv_all_plants_text)

        fun bindPlants(plant: Plant) {
            plantImage.setImageURI(Uri.parse(plant.image))
            plantName.text = plant.name
        }
    }
}