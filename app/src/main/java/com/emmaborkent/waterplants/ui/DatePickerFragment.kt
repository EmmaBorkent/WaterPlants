package com.emmaborkent.waterplants.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.emmaborkent.waterplants.database.ParseFormatDates
import java.time.LocalDate

class DatePickerFragment : DialogFragment() {
    private val classNameTag: String = DatePickerFragment::class.java.simpleName

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val date: LocalDate = getDate()
        val year = date.year
        val month = date.monthValue - 1
        val day = date.dayOfMonth
        return DatePickerDialog(
            activity!!,
            activity as AddEditPlantActivity,
            year,
            month,
            day
        )
    }

    private fun getDate(): LocalDate {
        return if (arguments != null) {
            val dateFromArguments = arguments?.getString("DATE")
            Log.d(classNameTag, "Argument is $dateFromArguments")
            ParseFormatDates().stringToDateLocalized(dateFromArguments!!)
        } else {
            LocalDate.now()
        }
    }
}