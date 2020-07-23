package dev.nikhi1.recipe.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.nikhi1.recipe.core.Result
import dev.nikhi1.recipe.core.UIState
import dev.nikhi1.recipe.onboarding.data.DietRepository
import dev.nikhi1.recipe.onboarding.data.model.DietResponse
import dev.nikhi1.recipe.onboarding.ui.DietPresenter
import dev.nikhi1.recipe.onboarding.ui.OnboardingViewModel
import dev.nikhi1.recipe.onboarding.ui.OnboardingViewState
import dev.nikhi1.recipe.test_shared.utils.getOrAwaitValue
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class OnboardingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: OnboardingViewModel

    private val dataRepository: DietRepository = mockk()
    private val dietPresenter: DietPresenter = mockk()
    private val testDispatchProvider = TestDispatchProvider()

    @Before
    fun setup() {
        clearMocks(dataRepository, dietPresenter)
        viewModel = OnboardingViewModel(dietPresenter, dataRepository, testDispatchProvider)
    }

    @Test
    fun `fetch diets`() {
        runBlocking {
            every { dietPresenter.map(any()) } returns TestData.dietViewTypes
            val uiState = OnboardingViewState(
                uiState = UIState.Content, diets = dietPresenter.map(TestData.dietResponse.diets))
            coEvery {  dataRepository.getDiets() } returns Result.Success(TestData.dietResponse.diets)
            viewModel.fetchDiets()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `empty diets`() {
        runBlocking {
            val uiState =
                OnboardingViewState(uiState = UIState.Empty)
            coEvery {  dataRepository.getDiets() } returns Result.Success(DietResponse.EMPTY.diets)
            viewModel.fetchDiets()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `no diets`() {
        runBlocking {
            val uiState =
                OnboardingViewState(uiState = UIState.Empty)
            coEvery {  dataRepository.getDiets() } returns Result.Success(null)
            viewModel.fetchDiets()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `failed fetch diets`() {
        runBlocking {
            val ioException = IOException()
            val uiState = OnboardingViewState(
                uiState = UIState.Error(ioException)
            )
            coEvery {  dataRepository.getDiets() } returns Result.Failure(ioException)
            viewModel.fetchDiets()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }
}