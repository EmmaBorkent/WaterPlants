package com.emmaborkent.waterplants.addeditplant

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.emmaborkent.waterplants.model.DateConverter
import com.emmaborkent.waterplants.util.DATE_PICKER_DATE
import java.time.LocalDate

class DatePickerDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val date: LocalDate = getDate()
        val year = date.year
        val month = date.monthValue - 1
        val day = date.dayOfMonth
        return DatePickerDialog(
            requireActivity(),
            targetFragment as OnDateSetListener?,
            year,
            month,
            day
        )
    }

    private fun getDate(): LocalDate {
        return if (arguments != null) {
            val dateFromArguments = arguments?.getString(DATE_PICKER_DATE)
            val dateConverter = DateConverter.getDateConverterInstance()
            dateConverter.viewDateStringToLocalDate(dateFromArguments!!)
        } else {
            throw IllegalArgumentException("No Valid Date")
        }
    }
}