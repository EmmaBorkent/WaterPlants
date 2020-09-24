package com.emmaborkent.waterplants.allplants

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.RecyclerViewAllPlantsBinding
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.util.PlantClickListener
import timber.log.Timber

class PlantsAllAdapter :
    RecyclerView.Adapter<PlantsAllAdapter.PlantsHolder>() {

    private lateinit var binding: RecyclerViewAllPlantsBinding
    private var plants = emptyList<Plant>()
    private lateinit var listener: PlantClickListener

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
        holder.bindPlants(plants[position])
    }

    internal fun setPlants(plants: List<Plant>) {
        this.plants = plants
        // TODO: 3-9-2020 remove notify data set changed
        notifyDataSetChanged()
    }

    inner class PlantsHolder(binding: RecyclerViewAllPlantsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPlants(plant: Plant) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val plantBitmapImage = BitmapFactory.decodeFile(plant.image, bitmapOptions)

            binding.apply {
                imageRvAllPlants.setImageBitmap(plantBitmapImage)
                textRvAllPlants.text = plant.name
                cardRvAllPlants.setOnClickListener {
                    val id = plant.id
                    listener.onItemClick(id)
                    Timber.i("Adapter Plant ID: $id")
                }
            }
        }
    }

    fun setOnItemClickListener(listener: PlantClickListener) {
        this.listener = listener
    }
}