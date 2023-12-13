package com.example.automaat.models.Car

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.automaat.models.Car.CarDbHelper.FeedReaderContract.FeedEntry

class CarDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    object FeedReaderContract {
        // Table contents are grouped together in an anonymous object.
        object FeedEntry : BaseColumns {
            const val TABLE_NAME = "cars"
            const val ID = "id"
            const val BRAND = "brand"
            const val MODEL = "model"
        }
    }

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.ID} INTEGER," +
                "${FeedEntry.BRAND} TEXT," +
                "${FeedEntry.MODEL} TEXT)" + ";"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }

    fun insertCar(car: CarModel): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(FeedEntry.ID, car.id)
            put(FeedEntry.BRAND, car.brand)
            put(FeedEntry.MODEL, car.model)
        }
        val success = db.insert(FeedEntry.TABLE_NAME, null, values)
        db.close()

        if (success == -1L) {
            throw Exception("Failed to insert car")
        }

        return success
    }

    fun insertDummyCars() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(FeedEntry.ID, CarModel.getAutoId())
            put(FeedEntry.BRAND, "BMW")
            put(FeedEntry.MODEL, "X5")
        }
        val success = db.insert(FeedEntry.TABLE_NAME, null, values)
        db.close()

        if (success == -1L) {
            throw Exception("Failed to insert car")
        }
    }

    @SuppressLint("Range")
    fun getAllCars(): ArrayList<CarModel> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${FeedEntry.TABLE_NAME}", null)
        val cars = ArrayList<CarModel>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(FeedEntry.ID))
                val brand = cursor.getString(cursor.getColumnIndex(FeedEntry.BRAND))
                val model = cursor.getString(cursor.getColumnIndex(FeedEntry.MODEL))
                cars.add(CarModel(id, brand, model))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return cars
    }
}
