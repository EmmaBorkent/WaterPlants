package com.emmaborkent.waterplants.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.RecyclerViewAllPlantsBinding
import com.emmaborkent.waterplants.model.Plant

class PlantsAllAdapter(private val clickListener: () -> Unit) :
    RecyclerView.Adapter<PlantsAllAdapter.PlantsHolder>() {

    private lateinit var binding: RecyclerViewAllPlantsBinding
    private var plants = emptyList<Plant>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlantsHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.recycler_view_all_plants, parent, false)
        return PlantsHolder(binding)
    }

    override fun getItemCount() = plants.size

    override fun onBindViewHolder(holder: PlantsHolder, position: Int) {
        holder.bindPlants(plants[position], clickListener)
    }

    internal fun setPlants(plants: List<Plant>) {
        this.plants = plants
        notifyDataSetChanged()
    }

    inner class PlantsHolder(binding: RecyclerViewAllPlantsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPlants(plant: Plant, clickListener: () -> Unit) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val plantBitmapImage = BitmapFactory.decodeFile(plant.image, bitmapOptions)

            binding.apply {
                imageRvAllPlants.setImageBitmap(plantBitmapImage)
                textRvAllPlants.text = plant.name
            }

            // TODO: 11-7-2020 Change itemView to binding with Click Listener
            itemView.setOnClickListener { clickListener() }
        }
    }
}