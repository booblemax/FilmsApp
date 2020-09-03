package com.example.filmsapp.base

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.R
import com.example.filmsapp.base.mvi.BaseIntent
import com.example.filmsapp.base.mvi.IState
import com.example.filmsapp.base.mvi.Intention
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
abstract class BaseViewModel<S : IState, I : Intention>(
    protected val dispatcherProvider: DispatcherProvider,
    initialState: S
) : ViewModel() {

    private val intents: Channel<I> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> get() = _state

    private val _showSnackbar = MutableLiveData<Event<Int>>()
    val showSnackbar: LiveData<Event<Int>> = _showSnackbar

    private val job: Job = Job()
    private val defaultExceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }

    protected val baseScope = CoroutineScope(job + dispatcherProvider.main() + defaultExceptionHandler)

    init {
        handleIntentions()
    }

    private fun handleIntentions() {
        baseScope.launch {
            intents.consumeAsFlow().collect {
                if (it is BaseIntent.Error) handleException(it.error)
                else processIntention(it)
            }
        }
    }

    open suspend fun processIntention(intent: I) {
        Timber.i(intent::class.simpleName)
    }

    protected suspend fun reduce(handler: suspend (intent: S) -> S) {
        _state.value = handler(_state.value)
    }

    fun pushIntent(intent: I) {
        baseScope.launch {
            intents.send(intent)
        }
    }

    open fun handleException(exception: Throwable) {
        Timber.e(exception)
        postMessage(R.string.error)
    }

    override fun onCleared() {
        baseScope.cancel()
    }

    protected fun postMessage(@StringRes message: Int) {
        _showSnackbar.value = Event(message)
    }

    fun runDelayed(timeDelay: Long = TIME_DELAYED_MILLIS, action: () -> Unit) {
        baseScope.launch {
            delay(timeDelay)
            action()
        }
    }

    companion object {
        const val TIME_DELAYED_MILLIS = 3000L
    }
}
