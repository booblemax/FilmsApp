package com.example.filmsapp.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    protected val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: LiveData<String> = _showSnackbar

    private val job: Job = Job()
    private val defaultExceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }
    protected val baseContext = CoroutineScope(job + Dispatchers.Main + defaultExceptionHandler)

    protected open fun handleException(exception: Throwable) {
        Timber.e(exception)
    }

    override fun onCleared() {
        baseContext.cancel()
    }
}
