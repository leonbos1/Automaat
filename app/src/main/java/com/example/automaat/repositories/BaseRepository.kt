package com.example.automaat.repositories

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.automaat.models.rental.RentalModel

abstract class BaseRepository<T>(
    context: Context,
    private val tableName: String,
    private val databaseVersion: Int
) : SQLiteOpenHelper(context, DATABASE_NAME, null, databaseVersion) {

    companion object {
        const val DATABASE_NAME = "FeedReader.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the table based on the provided tableName
        db.execSQL(createTableQuery(tableName))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the existing table and recreate it if the database version is updated
        db.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }

    abstract fun createTableQuery(tableName: String): String

    abstract fun getEntityFromCursor(cursor: Cursor): T

    @SuppressLint("Range")
    fun <T> getAllEntities(
        tableName: String,
        cursorToEntity: (Cursor) -> T
    ): ArrayList<T> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $tableName", null)
        val entities = ArrayList<T>()

        if (cursor.moveToFirst()) {
            do {
                entities.add(cursorToEntity(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return entities
    }

    fun insert(values: ContentValues): Long {
        val db = writableDatabase
        val success = db.insert(tableName, null, values)
        db.close()

        if (success == -1L) {
            throw Exception("Failed to insert data into $tableName")
        }

        return success
    }
}
