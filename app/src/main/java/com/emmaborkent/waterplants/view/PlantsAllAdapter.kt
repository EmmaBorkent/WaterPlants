package com.emmaborkent.waterplants.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.model.PlantEntity

class PlantsAllAdapter internal constructor(
    private val clickListener: (PlantEntity) -> Unit
) : RecyclerView.Adapter<PlantsAllAdapter.PlantsHolder>() {

    private var plants = emptyList<PlantEntity>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlantsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_all_plants,
            parent, false
        )
        return PlantsHolder(itemView)
    }

    override fun getItemCount() = plants.size

    override fun onBindViewHolder(holder: PlantsHolder, position: Int) {
        holder.bindPlants(plants[position], clickListener)
    }

    internal fun setPlants(plants: List<PlantEntity>) {
        this.plants = plants
        notifyDataSetChanged()
    }

    inner class PlantsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val plantImage = itemView
            .findViewById<ImageView>(R.id.image_rv_all_plants)
        private val plantName = itemView
            .findViewById<TextView>(R.id.text_rv_all_plants)

        fun bindPlants(plant: PlantEntity, clickListener: (PlantEntity) -> Unit) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val plantBitmapImage = BitmapFactory.decodeFile(plant.image, bitmapOptions)
            plantImage.setImageBitmap(plantBitmapImage)
            plantName.text = plant.name
            itemView.setOnClickListener { clickListener(plant) }
        }
    }
}