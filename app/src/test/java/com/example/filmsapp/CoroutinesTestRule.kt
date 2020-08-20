package com.example.filmsapp

import com.example.filmsapp.domain.dispatcherProvider.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.coroutines.ContinuationInterceptor

@ExperimentalCoroutinesApi
class CoroutinesTestRule : TestRule, TestCoroutineScope by TestCoroutineScope() {

    val dispatcher = coroutineContext[ContinuationInterceptor] as TestCoroutineDispatcher

    val testDispatcherProvider = object : DispatcherProvider {
        override fun default(): CoroutineDispatcher = dispatcher
        override fun io(): CoroutineDispatcher = dispatcher
        override fun main(): CoroutineDispatcher = dispatcher
        override fun unconfined(): CoroutineDispatcher = dispatcher
    }

    override fun apply(base: Statement?, description: Description?): Statement =
        object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                Dispatchers.setMain(dispatcher)

                base?.evaluate()

                Dispatchers.resetMain()
                cleanupTestCoroutines()
            }
        }
}
