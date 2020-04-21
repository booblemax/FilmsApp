package com.example.filmsapp.domain

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.example.filmsapp.domain.exceptions.RetrofitException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType> {

    fun asFlow() = flow<Resource<ResultType>> {
        emit(Resource.LOADING())

        val dbValue = loadFromDb().first()
        if (shouldFetch(dbValue)) {
            emit(Resource.LOADING(dbValue))
            val response = fetchFromNetwork()
            if (response.isSuccessful) {
                saveNetworkResult(processResponse(response))
                loadFromDb().collect { emit(Resource.SUCCESS(it)) }
            } else {
                onFetchFailed()
                loadFromDb().collect {
                    emit(
                        Resource.ERROR(
                            RetrofitException(
                                response.code(),
                                response.message()
                            ), it
                        )
                    )
                }
            }
        } else {
            loadFromDb().collect { emit(Resource.SUCCESS(it)) }
        }
    }

    protected open fun onFetchFailed() {
        // Implement in sub-classes to handle errors
    }

    @WorkerThread
    protected open fun processResponse(response: Response<RequestType>) = response.body()

    @WorkerThread
    protected abstract suspend fun saveNetworkResult(item: RequestType?)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flow<ResultType>

    @MainThread
    protected abstract suspend fun fetchFromNetwork(): Response<RequestType>
}