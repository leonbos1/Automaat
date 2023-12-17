package com.example.automaat.repositories

import android.content.Context
import android.provider.BaseColumns
import com.example.automaat.models.car.CarModel

class RentalRepository(context: Context) : BaseRepository<CarModel>(
    context,
    FeedEntry.TABLE_NAME,
    DATABASE_VERSION
) {
    override fun createTableQuery(tableName: String): String {
        return "CREATE TABLE $tableName (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.ID} INTEGER," +
                "${FeedEntry.CODE} TEXT," +
                "${FeedEntry.LONGITUDE} TEXT," +
                "${FeedEntry.LATITUDE} TEXT," +
                "${FeedEntry.FROMDATE} TEXT," +
                "${FeedEntry.TODATE} TEXT," +
                "${FeedEntry.RENTALSTATE} TEXT)"
    }

    companion object {
        private var instance: CarRepository? = null

        fun getInstance(context: Context): CarRepository {
            if (instance == null) {
                instance = CarRepository(context)
            }
            return instance!!
        }

        const val DATABASE_VERSION = 1
    }

    object FeedEntry {
        const val TABLE_NAME = "rentals"
        const val ID = "id"
        const val CODE = "code"
        const val LONGITUDE = "longitude"
        const val LATITUDE = "latitude"
        const val FROMDATE = "fromDate"
        const val TODATE = "toDate"
        const val RENTALSTATE = "rentalState"
    }
}