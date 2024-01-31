package com.example.automaat.helpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateHelper {
    companion object {
        fun getDates(): List<String> {
            val today = LocalDate.now()
            val dateList = mutableListOf<String>()
            for (i in 0..100) {
                dateList.add(today.plusDays(i.toLong()).toString())
            }

            return dateList
        }

        fun getPossibleEndDates(startDate: String): List<String> {
            var start = LocalDate.parse(startDate)
            start = start.plusDays(1) // A reservation must be at least 1 day long

            val dateList = mutableListOf<String>()
            for (i in 0..100) {
                dateList.add(start.plusDays(i.toLong()).toString())
            }
            return dateList
        }

        fun getDaysBetween(startDate: String, endDate: String): Long {
            if (startDate == "" || endDate == "" || startDate == "Start date" || endDate == "End date") {
                return 0
            }

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val start = LocalDate.parse(startDate, formatter)
            val end = LocalDate.parse(endDate, formatter)

            val daysBetween = end.toEpochDay() - start.toEpochDay()

            if (daysBetween < 0) {
                return 0
            }

            return daysBetween
        }
    }
}