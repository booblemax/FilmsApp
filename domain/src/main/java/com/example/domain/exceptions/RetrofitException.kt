package com.example.domain.exceptions

class RetrofitException(
    val code: Int,
    message: String? = null,
    val exception: Exception? = null
) : Exception(message)
