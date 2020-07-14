package dev.nikhi1.recipe.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.nikhi1.recipe.core.Result
import dev.nikhi1.recipe.core.UIState
import dev.nikhi1.recipe.onboarding.data.DataRepository
import dev.nikhi1.recipe.onboarding.data.model.SubCategoryResponse
import dev.nikhi1.recipe.onboarding.ui.CategoryPresenter
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

    val dataRepository: DataRepository = mockk()
    val categoryPresenter: CategoryPresenter = mockk()

    @Before
    fun setup() {
        clearMocks(dataRepository, categoryPresenter)
        viewModel = OnboardingViewModel(categoryPresenter, dataRepository)
    }

    @Test
    fun `fetch topics or interests`() {
        coroutinesRule.runBlocking {
            every { categoryPresenter.map(any()) } returns TestData.categoryViewTypes
            val uiState = OnboardingViewState(
                uiState = UIState.Content, categories = categoryPresenter.map(TestData.subcategoryByParent))
            coEvery {  dataRepository.getTopics(any()) } returns Result.Success(TestData.subcategoryNetworkResponse)
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `empty topics or interests`() {
        coroutinesRule.runBlocking {
            val uiState =
                OnboardingViewState(uiState = UIState.Empty)
            coEvery {  dataRepository.getTopics(any()) } returns Result.Success(TestData.emptySubCategoryResponse)
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `no topics or interests`() {
        coroutinesRule.runBlocking {
            val uiState =
                OnboardingViewState(uiState = UIState.Empty)
            coEvery {  dataRepository.getTopics(any()) } returns Result.Success(null)
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `failed fetch topics or interests`() {
        coroutinesRule.runBlocking {
            val ioException = IOException()
            val uiState = OnboardingViewState(
                uiState = UIState.Error(ioException)
            )
            coEvery {  dataRepository.getTopics(any()) } returns Result.Failure(ioException)
            viewModel.fetchTopics()
            assertEquals(uiState, viewModel.viewState.getOrAwaitValue())
        }
    }

    @Test
    fun `properly sort subCategories under a parent category`() {
        coroutinesRule.runBlocking {
            val subs = TestData.subcategoryNetworkResponse.subcategories
            assertEquals(TestData.subcategoryByParent, viewModel.groupSubcategoryByParentCategory(subs))
        }
    }

    @Test
    fun `return empty list for empty input list for subcategories sorting`() {
        coroutinesRule.runBlocking {
            val subs = SubCategoryResponse.EMPTY.subcategories
            assertEquals(0, viewModel.groupSubcategoryByParentCategory(subs).size)
        }
    }
}