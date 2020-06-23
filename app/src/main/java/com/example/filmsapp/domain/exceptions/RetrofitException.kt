package com.example.filmsapp.domain.exceptions

class RetrofitException(
    val code: Int,
    message: String? = null,
    val exception: Exception? = null
) :
    Exception(message)
