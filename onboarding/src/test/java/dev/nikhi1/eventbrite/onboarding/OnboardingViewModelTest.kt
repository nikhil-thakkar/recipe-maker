package dev.nikhi1.eventbrite.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.nikhi1.eventbrite.core.Result
import dev.nikhi1.eventbrite.core.Success
import dev.nikhi1.eventbrite.test_shared.utils.MainCoroutineRule
import dev.nikhi1.eventbrite.test_shared.utils.getOrAwaitValue
import dev.nikhi1.eventbrite.test_shared.utils.runBlocking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OnboardingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutineRule()


    lateinit var viewModel: OnboardingViewModel

    @Before
    fun setup() {
        viewModel = OnboardingViewModel(FakeRepo())
    }

    @Test
    fun `fetch topics or interests`() {
        coroutinesRule.runBlocking{
            viewModel.fetchTopics()
            val list: List<String> = viewModel.topics.getOrAwaitValue()
            Assert.assertEquals(1, list.size)
        }
    }
}

class FakeRepo : DataRepository {
    override suspend fun getTopics(): Result<List<String>> {
        return Success(listOf("nikhil"))
    }
}