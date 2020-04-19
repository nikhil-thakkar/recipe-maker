package dev.nikhi1.eventbrite.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.nikhi1.eventbrite.core.Result
import dev.nikhi1.eventbrite.core.UIState
import dev.nikhi1.eventbrite.onboarding.data.DataRepository
import dev.nikhi1.eventbrite.onboarding.ui.CategoryPresenter
import dev.nikhi1.eventbrite.onboarding.ui.OnboardingViewModel
import dev.nikhi1.eventbrite.onboarding.ui.OnboardingViewState
import dev.nikhi1.eventbrite.test_shared.utils.MainCoroutineRule
import dev.nikhi1.eventbrite.test_shared.utils.getOrAwaitValue
import dev.nikhi1.eventbrite.test_shared.utils.runBlocking
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
}