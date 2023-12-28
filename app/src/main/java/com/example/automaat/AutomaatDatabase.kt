package com.example.automaat

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.RentalModel
import com.example.automaat.repositories.CarDao
import com.example.automaat.repositories.RentalDao

@Database(
    entities = [
        CarModel::class,
        RentalModel::class
    ],
    version = 1,
    exportSchema = false
)


abstract class AutomaatDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun rentalDao(): RentalDao

    companion object {
        @Volatile
        private var INSTANCE: AutomaatDatabase? = null

        fun getDatabase(context: Context): AutomaatDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AutomaatDatabase::class.java,
                    "automaat_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}