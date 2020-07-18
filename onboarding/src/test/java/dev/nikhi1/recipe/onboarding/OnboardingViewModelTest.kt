package dev.nikhi1.recipe.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.nikhi1.recipe.core.Result
import dev.nikhi1.recipe.core.UIState
import dev.nikhi1.recipe.onboarding.data.DietRepository
import dev.nikhi1.recipe.onboarding.data.model.DietResponse
import dev.nikhi1.recipe.onboarding.ui.DietPresenter
import dev.nikhi1.recipe.onboarding.ui.OnboardingViewModel
import dev.nikhi1.recipe.onboarding.ui.OnboardingViewState
import dev.nikhi1.recipe.test_shared.utils.MainCoroutineRule
import dev.nikhi1.recipe.test_shared.utils.getOrAwaitValue
import dev.nikhi1.recipe.test_shared.utils.runBlocking
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
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

    val dataRepository: DietRepository = mockk()
    val dietPresenter: DietPresenter = mockk()

    @Before
    fun setup() {
        clearMocks(dataRepository, dietPresenter)
        viewModel = OnboardingViewModel(dietPresenter, dataRepository)
    }

    @Test
    fun `fetch diets`() {
        coroutinesRule.runBlocking {
            every { dietPresenter.map(any()) } returns TestData.dietViewTypes
            val uiState = OnboardingViewState(
                uiState = UIState.Content, diets = dietPresenter.map(TestData.dietResponse.diets))
            coEvery {  dataRepository.getDiets() } returns Result.Success(TestData.dietResponse.diets)
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `empty diets`() {
        coroutinesRule.runBlocking {
            val uiState =
                OnboardingViewState(uiState = UIState.Empty)
            coEvery {  dataRepository.getDiets() } returns Result.Success(DietResponse.EMPTY.diets)
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `no diets`() {
        coroutinesRule.runBlocking {
            val uiState =
                OnboardingViewState(uiState = UIState.Empty)
            coEvery {  dataRepository.getDiets() } returns Result.Success(null)
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `failed fetch diets`() {
        coroutinesRule.runBlocking {
            val ioException = IOException()
            val uiState = OnboardingViewState(
                uiState = UIState.Error(ioException)
            )
            coEvery {  dataRepository.getDiets() } returns Result.Failure(ioException)
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }
}