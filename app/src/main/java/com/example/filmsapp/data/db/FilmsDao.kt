package com.example.filmsapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FilmsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg films: FilmDataModel)

    @Update
    fun update(film: FilmDataModel)

    @Delete
    fun delete(vararg films: FilmDataModel)

    @Query("SELECT * FROM films WHERE id = :id")
    fun getFilm(id: String): FilmDataModel

    @Query("SELECT * FROM films")
    fun getFilms(): List<FilmDataModel>
}
