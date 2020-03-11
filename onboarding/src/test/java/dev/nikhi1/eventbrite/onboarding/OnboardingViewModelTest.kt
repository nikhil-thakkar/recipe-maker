package dev.nikhi1.eventbrite.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.nikhi1.eventbrite.core.Result
import dev.nikhi1.eventbrite.core.UIState
import dev.nikhi1.eventbrite.onboarding.data.DataRepository
import dev.nikhi1.eventbrite.test_shared.utils.MainCoroutineRule
import dev.nikhi1.eventbrite.test_shared.utils.getOrAwaitValue
import dev.nikhi1.eventbrite.test_shared.utils.runBlocking
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class OnboardingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    lateinit var viewModel: OnboardingViewModel

    lateinit var mock: DataRepository

    @Before
    fun setup() {
        mock = mockk()
        viewModel = OnboardingViewModel(mock)
    }

    @Test
    fun `fetch topics or interests`() {
        coroutinesRule.runBlocking {
            val uiState = OnboardingViewState(uiState = UIState.Content, categories = listOf("nikhil"))
            coEvery {  mock.getTopics() } returns Result.Success(listOf("nikhil"))
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `empty topics or interests`() {
        coroutinesRule.runBlocking {
            val uiState = OnboardingViewState(uiState = UIState.Empty)
            coEvery {  mock.getTopics() } returns Result.Success(listOf())
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `failed fetch topics or interests`() {
        coroutinesRule.runBlocking {
            val uiState = OnboardingViewState(uiState = UIState.Error)
            coEvery {  mock.getTopics() } returns Result.Failure(IOException())
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }
}