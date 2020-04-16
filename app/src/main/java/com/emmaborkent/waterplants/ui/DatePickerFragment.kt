package com.emmaborkent.waterplants.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import java.time.LocalDate
import java.util.*


class DatePickerFragment : DialogFragment() {

    // TODO: 16-4-2020 Improve onCreateDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val year: Int
        val month: Int
        val day: Int

        if (arguments != null) {
            val getDate = arguments?.getString("DATE")
            Log.d("DatePickerFragment", "Argument is $getDate")

            val date = LocalDate.parse(getDate)
            year = date.year
            month = date.monthValue - 1
            day = date.dayOfMonth
        } else {
            val date = Calendar.getInstance()
            year = date.get(Calendar.YEAR)
            month = date.get(Calendar.MONTH)
            day = date.get(Calendar.DAY_OF_MONTH)
        }

        return DatePickerDialog(
            activity!!,
            activity as AddEditPlantActivity,
            year,
            month,
            day
        )
    }

}