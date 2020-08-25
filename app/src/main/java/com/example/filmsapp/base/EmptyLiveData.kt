package com.example.filmsapp.base

import androidx.lifecycle.MutableLiveData

class EmptyLiveData : MutableLiveData<Any>() {

    fun post() {
        postValue(true)
    }
}
