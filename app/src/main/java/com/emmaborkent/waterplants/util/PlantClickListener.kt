package com.emmaborkent.waterplants.util

import com.emmaborkent.waterplants.model.Plant

class PlantsTodayListener(val clickListener: (plantId: Int) -> Unit) {
    // When a view that displays a list item is clicked, the view calls this onClick() function
    // Add a function argument plant of type Plant to onClick(). The view knows what item it is
    // displaying, and the information needs to be passed on for handling the click.
    // To define what onClick() does, provide a clickListener callback in the constructor of the
    // Listener and assign it to onClick().
    // Giving the lambda that handles the click a name, clickListener, helps keep track of it as it
    // is passed between classes. The clickListener callback only needs the plant.id to access
    // data from the database.
    fun onClick(plant: Plant) = clickListener(plant.id)
}