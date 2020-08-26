package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FilmDataModel::class],
    version = FilmsDb.DATABASE_VERSION
)
abstract class FilmsDb : RoomDatabase() {

    abstract fun filmsDao(): FilmsDao

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "films_db"
    }
}
