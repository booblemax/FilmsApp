package com.example.filmsapp.data.datasource

import com.example.filmsapp.data.db.FilmDataModel
import com.example.filmsapp.data.remote.response.films.FilmDto
import com.example.filmsapp.ui.base.models.YoutubeFilmModel

val latest = FilmDto()
val populars = listOf(
    FilmDto(id = 591, title = "g4T095W", backdropPath = "JYDPCx5"),
    FilmDto(id = 212, title = "Q9jK0Cr8", backdropPath = "mw3Qf3l6"),
    FilmDto(id = 978, title = "o10h", backdropPath = "9y8ReDT"),
    FilmDto(id = 811, title = "s6nn", backdropPath = "NY8nDGyP")
)
val toprated = listOf(
    FilmDto(id = 325, title = "1ov", backdropPath = "4zCSA"),
    FilmDto(id = 959, title = "rM6bG", backdropPath = "sMN"),
    FilmDto(id = 830, title = "eycobJY", backdropPath = "T8luQ"),
    FilmDto(id = 221, title = "VoXP2", backdropPath = "5lCNCQ"),
    FilmDto(id = 303, title = "4CYBATJ", backdropPath = "0l9e25")
)
val upcoming = listOf(
    FilmDto(id = 61, title = "FB3Q4X", backdropPath = "Rbw"),
    FilmDto(id = 402, title = "Z9qmruv", backdropPath = "yt3L1"),
    FilmDto(id = 798, title = "6hED2", backdropPath = "t6NVfMSR"),
    FilmDto(id = 632, title = "2rT9", backdropPath = "hM7dO3i"),
    FilmDto(id = 360, title = "rqt7O", backdropPath = "cdtM")
)

val favorites = listOf(
    FilmDataModel("591", "YJQ60UK", "HIea9W22"),
    FilmDataModel("I63Zx18", "n4XW", "SfVzv5W"),
    FilmDataModel("94b", "g3m0hC", "8ie97ofW"),
    FilmDataModel("SO4", "o97smPV", "M2gLc5Qc")
)

val youtubeDataModel = YoutubeFilmModel("Film", "1", "1")
