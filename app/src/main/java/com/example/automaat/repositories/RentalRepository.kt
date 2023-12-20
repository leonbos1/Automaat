package com.example.automaat.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.example.automaat.models.car.CarModel
import com.example.automaat.models.rental.RentalModel
import java.time.LocalDate

class RentalRepository(context: Context) : BaseRepository<RentalModel>(
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

    @SuppressLint("Range")
    override fun getEntityFromCursor(cursor: Cursor): RentalModel {
        val id = cursor.getInt(cursor.getColumnIndex(FeedEntry.ID))
        val code = cursor.getString(cursor.getColumnIndex(FeedEntry.CODE))
        val longitude = cursor.getFloat(cursor.getColumnIndex(FeedEntry.LONGITUDE))
        val latitude = cursor.getFloat(cursor.getColumnIndex(FeedEntry.LATITUDE))
        val fromDateString = cursor.getString(cursor.getColumnIndex(FeedEntry.FROMDATE))
        val fromDate = LocalDate.parse(fromDateString)
        val toDateString = cursor.getString(cursor.getColumnIndex(FeedEntry.TODATE))
        val toDate = LocalDate.parse(toDateString)
        val rentalState = cursor.getInt(cursor.getColumnIndex(FeedEntry.RENTALSTATE))

        return RentalModel(id, code, longitude, latitude, fromDate, toDate, rentalState)
    }
}