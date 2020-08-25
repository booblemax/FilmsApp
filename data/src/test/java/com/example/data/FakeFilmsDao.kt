package com.example.data

import com.example.data.db.FilmDataModel
import com.example.data.db.FilmsDao

class FakeFilmsDao(private val films: MutableList<FilmDataModel>) : FilmsDao {

    override fun insert(vararg films: FilmDataModel) {
        this.films.addAll(films)
    }

    override fun update(film: FilmDataModel) {
        val index = films.indexOfFirst { it.id == film.id }
        films[index] = film
    }

    override fun delete(vararg films: FilmDataModel) {
        this.films.removeAll(films)
    }

    override fun getFilm(id: String): FilmDataModel? {
        return films.first { it.id == id }
    }

    override fun getFilms(): List<FilmDataModel> {
        return films
    }

    override fun contains(id: String): Int {
        return films.indexOfFirst { it.id == id }
    }
}
