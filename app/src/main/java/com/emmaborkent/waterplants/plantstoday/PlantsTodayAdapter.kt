package com.emmaborkent.waterplants.plantstoday

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.emmaborkent.waterplants.PlantViewModel
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.RecyclerViewMistPlantsBinding
import com.emmaborkent.waterplants.databinding.RecyclerViewWaterPlantsBinding
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.util.DateConverter
import com.emmaborkent.waterplants.util.PlantClickListener
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.time.LocalDate

class PlantsTodayAdapter(private val viewModel: PlantViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var plantsThatNeedWater = emptyList<Plant>()
    private var plantsThatNeedMist = emptyList<Plant>()
    private lateinit var listener: PlantClickListener
    private val dateConverterInstance = DateConverter.getDateConverterInstance()

    companion object {
        const val VIEW_TYPE_WATER = 0
        const val VIEW_TYPE_MIST = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position <= plantsThatNeedWater.size - 1) {
            VIEW_TYPE_WATER
        } else {
            VIEW_TYPE_MIST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WaterPlantsViewHolder -> holder.bindPlantsWater(
                plantsThatNeedWater[position]
            )
            is MistPlantsViewHolder -> holder.bindPlantsMist(
                plantsThatNeedMist[position - plantsThatNeedWater.size]
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

        fun bindPlantsWater(plant: Plant) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val plantBitmapImage = BitmapFactory.decodeFile(plant.image, bitmapOptions)

            binding.apply {
                imageRvWaterPlants.setImageBitmap(plantBitmapImage)
                textRvWaterPlantsName.text = plant.name

                textRvWaterPlantsDate.text =
                    dateConverterInstance.localDateToViewDateString(plant.waterDate)
                if (LocalDate.now() == plant.waterDate) {
                    textRvWaterPlantsDate.visibility = View.INVISIBLE
                }

                toggleRvWaterPlants.isChecked = false
                toggleRvWaterPlants.setOnClickListener { waterCheckBox(plant) }

                cardRvWaterPlants.setOnClickListener {
                    val id = plant.id
                    listener.onItemClick(id)
                    Timber.i("Adapter Plant ID: $id")
                }
            }
        }

        private fun waterCheckBox(plant: Plant) {
            if (binding.toggleRvWaterPlants.isChecked) {
                Timber.i("Plant ${plant.name}: Water Is Checked")
                viewModel.giveWater(plant)
                Snackbar
                    .make(binding.cardRvWaterPlants, "Plant was watered", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Undo") {
                        Timber.i("Plant ${plant.name}: Water Is NOT Checked")
                        binding.toggleRvWaterPlants.isChecked = false
                        viewModel.undoWaterGift(plant)
                    }
                    .show()
            }
        }
    }

    inner class MistPlantsViewHolder(private val binding: RecyclerViewMistPlantsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPlantsMist(plant: Plant) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            val plantBitmapImage = BitmapFactory.decodeFile(plant.image, bitmapOptions)

            binding.apply {
                imageRvMistPlants.setImageBitmap(plantBitmapImage)
                textRvMistPlantsName.text = plant.name

                textRvMistPlantsDate.text = dateConverterInstance.localDateToViewDateString(plant.mistDate)
                if (LocalDate.now() == plant.mistDate) {
                    textRvMistPlantsDate.visibility = View.INVISIBLE
                }

                toggleRvMistPlants.isChecked = false
                toggleRvMistPlants.setOnClickListener { mistCheckBox(plant) }

                cardRvMistPlants.setOnClickListener {
                    val id = plant.id
                    listener.onItemClick(id)
                    Timber.i("Adapter Plant ID: $id")
                }
            }
        }

        private fun mistCheckBox(plant: Plant) {
            if (binding.toggleRvMistPlants.isChecked) {
                Timber.i("Plant ${plant.name}: Mist Is Checked")
                viewModel.giveMist(plant)
                Snackbar
                    .make(binding.cardRvMistPlants, "Plant was misted", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Undo") {
                        Timber.i("Plant ${plant.name}: Mist Is NOT Checked")
                        binding.toggleRvMistPlants.isChecked = false
                        viewModel.undoMistGift(plant)
                    }
                    .show()
            }
        }
    }

    fun setOnItemClickListener(listener: PlantClickListener) {
        this.listener = listener
    }
}