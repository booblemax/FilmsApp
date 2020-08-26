package com.example.domain

sealed class Resource<T> {
    data class SUCCESS<T>(val data: T? = null) : Resource<T>()
    data class ERROR<T>(val message: Exception? = null, val data: T? = null) : Resource<T>()
    data class LOADING<T>(val data: T? = null) : Resource<T>()
}
