package com.emmaborkent.waterplants.view

import android.content.ClipData.Item
import androidx.recyclerview.widget.RecyclerView


class MyViewHolder(binding: ItemBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
    private val binding: ItemBinding
    fun bind(item: Item?) {
        binding.setItem(item)
        binding.executePendingBindings()
    }

    init {
        this.binding = binding
    }
}