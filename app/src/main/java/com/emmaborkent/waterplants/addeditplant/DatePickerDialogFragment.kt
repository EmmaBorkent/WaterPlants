package com.emmaborkent.waterplants.addeditplant

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.emmaborkent.waterplants.util.ParseFormatDates
import timber.log.Timber
import java.time.LocalDate

class DatePickerDialogFragment : DialogFragment() {

    // TODO: 25-6-2020 Create a ViewModel for DatePickerDialog
    // TODO: 16-7-2020 Create a destination from a DialogFragment in Navigation Graph
    // https://developer.android.com/guide/navigation/navigation-create-destinations
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val date: LocalDate = getDate()
        val year = date.year
        val month = date.monthValue - 1
        val day = date.dayOfMonth
        return DatePickerDialog(
            requireActivity(),
            activity as XAddEditPlantActivity,
            year,
            month,
            day
        )
    }

    private fun getDate(): LocalDate {
        return if (arguments != null) {
            val dateFromArguments = arguments?.getString("DATE")
            Timber.d("Argument is $dateFromArguments")
            ParseFormatDates().stringToDateLocalized(dateFromArguments!!)
        } else {
            LocalDate.now()
        }
    }
}