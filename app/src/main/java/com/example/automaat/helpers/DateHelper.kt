package com.example.automaat.helpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate

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
            val start = LocalDate.parse(startDate)
            val end = LocalDate.parse(endDate)
            return end.toEpochDay() - start.toEpochDay()
        }
    }
}