package com.realityexpander.daterangepicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.realityexpander.daterangepicker.databinding.ActivityMainBinding
import petrov.kristiyan.colorpicker.ColorPicker
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var startDate: Long? = null
    var endDate: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dateRangePicker.setOnClickListener {
            showDateRangePicker()
        }

        binding.datePicker.setOnClickListener {
            showDatePicker()
        }

        binding.pickColor.setOnClickListener {
            showColorPicker()
        }

    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun showColorPicker() {

        // Color Picker from: https://github.com/kristiyanP/colorpicker
        // Other color pickers: https://ourcodeworld.com/articles/read/932/top-10-best-android-color-picker-libraries

        ColorPicker(this).apply {
            setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int) {
                    if (color == 0) {
                        binding.tvRangeDate.text =
                            "Color: none selected"

                        return
                    }

                    binding.tvRangeDate.text =
                        "Color: ${java.lang.Integer.toHexString(color).uppercase(Locale.US)}\n"
                }

                override fun onCancel() {
                    binding.tvRangeDate.text =
                        "Color: cancelled"
                }
            })

            show()
        }
    }


    private fun showDateRangePicker() {

        val dateRangePicker =
                MaterialDatePicker
                        .Builder.dateRangePicker()
                        .setTitleText("Select Date Range")
                        .setSelection(
                            Pair(startDate ?: MaterialDatePicker.todayInUtcMilliseconds(),
                                 endDate   ?: MaterialDatePicker.todayInUtcMilliseconds()
                            )
                        )
                        .setCalendarConstraints(
                                CalendarConstraints.Builder()
                                        .setValidator(DateValidatorPointForward.now())
                                        .setEnd(Calendar.getInstance().apply {
                                                add(Calendar.YEAR, 1)
                                            }.timeInMillis)
                                        .build()
                        )
                        .build()

        dateRangePicker.show(
                supportFragmentManager,
                "date_range_picker"
        )

        dateRangePicker.addOnPositiveButtonClickListener { dateSelected ->
            startDate = dateSelected.first
            endDate = dateSelected.second

            if (startDate != null && endDate != null) {
                binding.tvRangeDate.text =
                        "StartDate: ${startDate!!.convertLongToTime()}\n" +
                        "EndDate: ${endDate!!.convertLongToTime()}"
            }
        }
    }

    private fun showDatePicker() {

        val datePicker =
            MaterialDatePicker
                .Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(
                    startDate ?: MaterialDatePicker.todayInUtcMilliseconds()
                )
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now())
                        .setEnd(Calendar.getInstance().apply {
                            add(Calendar.YEAR, 1)
                        }.timeInMillis)
                        .build()
                )
                .build()

        datePicker.show(
            supportFragmentManager,
            "date_picker"
        )

        datePicker.addOnPositiveButtonClickListener { dateSelected ->

            startDate = dateSelected

            if (startDate != null) {
                binding.tvRangeDate.text =
                    "Date: ${startDate!!.convertLongToTime()}\n"
            }
        }

    }


    fun Long.convertLongToTime(): String {
        val date = Date(this)
        val format = SimpleDateFormat(
                "MM/dd/yyyy",
                Locale.getDefault())
        return format.format(date)
    }
}