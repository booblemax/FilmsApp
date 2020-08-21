package com.example.filmsapp.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import timber.log.Timber

abstract class BaseViewModel(protected val dispatcherProvider: DispatcherProvider) : ViewModel() {

    private val _showSnackbar = MutableLiveData<Event<Int>>()
    val showSnackbar: LiveData<Event<Int>> = _showSnackbar

    private val job: Job = Job()
    private val defaultExceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }
    protected val baseContext = CoroutineScope(job + dispatcherProvider.main() + defaultExceptionHandler)

    protected open fun handleException(exception: Throwable) {
        Timber.e(exception)
        postMessage(R.string.error)
    }

    override fun onCleared() {
        baseContext.cancel()
    }

    protected fun postMessage(@StringRes message: Int) {
        _showSnackbar.value = Event(message)
    }
}
