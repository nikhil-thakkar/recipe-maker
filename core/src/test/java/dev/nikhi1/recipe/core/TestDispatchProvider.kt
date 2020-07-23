package dev.nikhi1.recipe.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class TestDispatchProvider : Dispatcher {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    override val io: CoroutineDispatcher
        get() = testCoroutineDispatcher

    override val default: CoroutineDispatcher
        get() = testCoroutineDispatcher

    override val main: CoroutineDispatcher
        get() = testCoroutineDispatcher
}
