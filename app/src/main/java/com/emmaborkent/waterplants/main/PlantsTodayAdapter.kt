package com.emmaborkent.waterplants.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.RecyclerViewMistPlantsBinding
import com.emmaborkent.waterplants.databinding.RecyclerViewWaterPlantsBinding
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.util.ParseFormatDates
import com.emmaborkent.waterplants.util.PlantsTodayListener

// Modify the constructor of the Adapter class to receive a val clickListener: PlantsTodayListener
// When the adapter binds the ViewHolder, it will need to provide it with this click listener
class PlantsTodayAdapter(
    private val viewModel: PlantViewModel,
    private val clickListener: PlantsTodayListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_WATER = 0
        const val VIEW_TYPE_MIST = 1
    }

    private val classNameTag: String =
        com.emmaborkent.waterplants.addeditplant.AddEditPlantActivity::class.java.simpleName
    private var plantsThatNeedWater = emptyList<Plant>()
    private var plantsThatNeedMist = emptyList<Plant>()

    override fun getItemViewType(position: Int): Int {
        return if (position <= plantsThatNeedWater.size - 1) {
            VIEW_TYPE_WATER
        } else {
            VIEW_TYPE_MIST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        return when (viewType) {
            VIEW_TYPE_WATER -> {
                val binding = DataBindingUtil.inflate<RecyclerViewWaterPlantsBinding>(
                    layoutInflater,
                    R.layout.recycler_view_water_plants,
                    parent,
                    false
                )
                WaterPlantsViewHolder(binding)
            }
            VIEW_TYPE_MIST -> {
                val binding = DataBindingUtil.inflate<RecyclerViewMistPlantsBinding>(
                    layoutInflater,
                    R.layout.recycler_view_mist_plants,
                    parent,
                    false
                )
                MistPlantsViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount() = plantsThatNeedWater.size + plantsThatNeedMist.size

    // In onBindViewHolder() update the call to holder.bind() to also pass the click listener to the
    // ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WaterPlantsViewHolder -> holder.bindPlantsWater(
                plantsThatNeedWater[position],
                clickListener
            )
            is MistPlantsViewHolder -> holder.bindPlantsMist(
                plantsThatNeedMist[position - plantsThatNeedWater.size],
                clickListener
            )
        }
    }

    internal fun setWaterPlants(waterPlants: List<Plant>) {
        this.plantsThatNeedWater = waterPlants
        notifyDataSetChanged()
    }

    internal fun setMistPlants(mistPlants: List<Plant>) {
        this.plantsThatNeedMist = mistPlants
        notifyDataSetChanged()
    }

    inner class WaterPlantsViewHolder(private val binding: RecyclerViewWaterPlantsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val todayString = ParseFormatDates()
            .getDefaultDateAsString()

        // Add the clickListener parameter to bind
        // Inside the ViewHolder class, inside bind() function, assign the clickListener to the
        // binding object
        fun bindPlantsWater(
            plant: Plant,
            clickListener: PlantsTodayListener
        ) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val plantBitmapImage = BitmapFactory.decodeFile(plant.image, bitmapOptions)

            binding.apply {
                imageRvWaterPlants.setImageBitmap(plantBitmapImage)
                textRvWaterPlantsName.text = plant.name

                textRvWaterPlantsDate.text =
                    ParseFormatDates().yearMonthDayStringToDayMonthYearString(plant.waterDate)
                if (todayString == plant.waterDate) {
                    textRvWaterPlantsDate.visibility = View.INVISIBLE
                }

                toggleRvWaterPlants.setOnClickListener { waterCheckBox(plant) }

            }

            binding.clickListener = clickListener

//            itemView.setOnClickListener {
//                clickListener(plant)
//            }
        }

        // TODO: 5-6-2020 Check if checked state is correctly recycled or not.
        //  https://blog.oziomaogbe.com/2017/10/18/android-handling-checkbox-state-in-recycler-views.html
        private fun waterCheckBox(plant: Plant) {
            if (binding.toggleRvWaterPlants.isChecked) {
                Log.i(classNameTag, "Plant ${plant.name}: Water Is Checked")
                viewModel.giveWater(plant)
            } else {
                Log.i(classNameTag, "Plant ${plant.name}: Water Is NOT Checked")
                viewModel.undoWaterGift(plant)
            }
        }
    }

    inner class MistPlantsViewHolder(private val binding: RecyclerViewMistPlantsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val todayString = ParseFormatDates()
            .getDefaultDateAsString()

        fun bindPlantsMist(
            plant: Plant,
            clickListener: PlantsTodayListener
        ) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val plantBitmapImage = BitmapFactory.decodeFile(plant.image, bitmapOptions)

            binding.apply {
                imageRvMistPlants.setImageBitmap(plantBitmapImage)
                textRvMistPlantsName.text = plant.name

                textRvMistPlantsDate.text =
                    ParseFormatDates().yearMonthDayStringToDayMonthYearString(plant.mistDate)
                if (todayString == plant.mistDate) {
                    textRvMistPlantsDate.visibility = View.INVISIBLE
                }

                toggleRvMistPlants.setOnClickListener { mistCheckBox(plant) }
            }

            binding.clickListener = clickListener
//            itemView.setOnClickListener { clickListener(plant) }
        }

        private fun mistCheckBox(plant: Plant) {
            if (binding.toggleRvMistPlants.isChecked) {
                Log.i(classNameTag, "Plant ${plant.name}: Mist Is Checked")
                viewModel.giveMist(plant)
            } else {
                Log.i(classNameTag, "Plant ${plant.name}: Mist Is NOT Checked")
                viewModel.undoMistGift(plant)
            }
        }
    }
}