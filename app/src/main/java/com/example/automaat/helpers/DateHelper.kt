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

        fun getPossibleEndDates(startDate: String): LiveData<List<String>> {
            val dates = MutableLiveData<List<String>>()
            val start = LocalDate.parse(startDate)
            val dateList = mutableListOf<String>()
            for (i in 0..100) {
                dateList.add(start.plusDays(i.toLong()).toString())
            }
            dates.value = dateList
            return dates
        }

        fun getDaysBetween(startDate: String, endDate: String): Long {
            val start = LocalDate.parse(startDate)
            val end = LocalDate.parse(endDate)
            return end.toEpochDay() - start.toEpochDay()
        }
    }
}