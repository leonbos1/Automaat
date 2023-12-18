package com.example.automaat.repositories

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.example.automaat.models.car.CarModel
import com.example.automaat.models.car.FilterModel
import com.example.automaat.models.rental.RentalModel

class CarRepository(context: Context) : BaseRepository<CarModel>(
    context,
    FeedEntry.TABLE_NAME,
    DATABASE_VERSION
) {
    override fun createTableQuery(tableName: String): String {
        return "CREATE TABLE $tableName (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.ID} INTEGER," +
                "${FeedEntry.BRAND} TEXT," +
                "${FeedEntry.MODEL} TEXT)" +
                "${FeedEntry.FUELTYPE} INTEGER)"
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
        const val TABLE_NAME = "cars"
        const val ID = "id"
        const val BRAND = "brand"
        const val MODEL = "model"
        const val FUELTYPE = "fuelType"
        const val RENTAL = "rental"
    }

    @SuppressLint("Range")
    fun filterCars(appliedFilters: FilterModel?): ArrayList<CarModel> {
        val db = readableDatabase
        var query = "SELECT * FROM ${FeedEntry.TABLE_NAME} WHERE "
        var queryAdded = false

        val brand = appliedFilters?.brand?.lowercase()
        val model = appliedFilters?.model?.lowercase()

        if (brand != null && brand != "all") {
            if (queryAdded) {
                query += " AND "
            }
            query += "LOWER(${FeedEntry.BRAND}) = '$brand'"
            queryAdded = true
        }

        if (model != null && model != "all") {
            if (queryAdded) {
                query += " AND "
            }
            query += "LOWER(${FeedEntry.MODEL}) = '$model'"
        }

        // Handle the case where both brand and model are "All"
        if ((brand == null || brand == "all") && (model == null || model == "all")) {
            query = "SELECT * FROM ${FeedEntry.TABLE_NAME}"
        }

        // Prevents syntax error when no filters are applied
        if (!queryAdded) {
            query = query.replace("WHERE", "")
        }

        val cursor = db.rawQuery(query, null)
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

    fun insertCar(car: CarModel) {
        val contentValues = ContentValues()
        contentValues.put(FeedEntry.ID, car.id)
        contentValues.put(FeedEntry.BRAND, car.brand)
        contentValues.put(FeedEntry.MODEL, car.model)

        insert(contentValues)
    }

    fun insertDummyCars() {
        val car1 = CarModel(1, "BMW", "X3", 1)
        val car2 = CarModel(2, "BMW", "M3", 0)
        val car3 = CarModel(3, "Alfa Romeo", "Guilia", 0)
        val car4 = CarModel(4, "Volkswagen", "Golf GTI", 0)
        val car5 = CarModel(5, "Toyota", "Prius", 2)

        insertCar(car1)
        insertCar(car2)
        insertCar(car3)
        insertCar(car4)
        insertCar(car5)
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
                val fuelType = cursor.getInt(cursor.getColumnIndex(FeedEntry.FUELTYPE))
                cars.add(CarModel(id, brand, model, fuelType))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return cars
    }

    @SuppressLint("Range")
    fun getAvailableBrands(): ArrayList<String> {
        val db = readableDatabase
        val cursor =
            db.rawQuery("SELECT DISTINCT ${FeedEntry.BRAND} FROM ${FeedEntry.TABLE_NAME}", null)
        val brands = ArrayList<String>()

        if (cursor.moveToFirst()) {
            do {
                val brand = cursor.getString(cursor.getColumnIndex(FeedEntry.BRAND))
                brands.add(brand)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return brands
    }

    @SuppressLint("Range")
    fun getAvailableModels(): ArrayList<String> {
        val db = readableDatabase
        val cursor =
            db.rawQuery("SELECT DISTINCT ${FeedEntry.MODEL} FROM ${FeedEntry.TABLE_NAME}", null)
        val models = ArrayList<String>()

        if (cursor.moveToFirst()) {
            do {
                val model = cursor.getString(cursor.getColumnIndex(FeedEntry.MODEL))
                models.add(model)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return models
    }

    @SuppressLint("Range")
    fun getAvailableModelsByBrand(brand: String): ArrayList<String> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT DISTINCT ${FeedEntry.MODEL} FROM ${FeedEntry.TABLE_NAME} WHERE ${FeedEntry.BRAND} = '$brand'",
            null
        )
        val models = ArrayList<String>()

        if (cursor.moveToFirst()) {
            do {
                val model = cursor.getString(cursor.getColumnIndex(FeedEntry.MODEL))
                models.add(model)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return models
    }

    @SuppressLint("Range")
    fun getRentalByCarId(car: CarModel): RentalModel {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM ${RentalRepository.FeedEntry.TABLE_NAME} WHERE ${RentalRepository.FeedEntry.ID} = ${car.rental}",
            null
        )

        var rental: RentalModel? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(RentalRepository.FeedEntry.ID))
            val code = cursor.getString(cursor.getColumnIndex(RentalRepository.FeedEntry.CODE))
            val longitude = cursor.getDouble(cursor.getColumnIndex(RentalRepository.FeedEntry.LONGITUDE))
            val latitude = cursor.getDouble(cursor.getColumnIndex(RentalRepository.FeedEntry.LATITUDE))
            //add more props here

            rental = RentalModel(id, code, longitude, latitude)
        }
        cursor.close()
        db.close()

        return rental ?: throw IllegalStateException("Rental not found")
    }
}