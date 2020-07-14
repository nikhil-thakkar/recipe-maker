package dev.nikhi1.recipe.onboarding.data

import dev.nikhi1.recipe.core.Result
import dev.nikhi1.recipe.onboarding.TestData
import dev.nikhi1.recipe.test_shared.utils.MainCoroutineRule
import dev.nikhi1.recipe.test_shared.utils.runBlocking
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CategoryRepositoryTest {

    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    lateinit var categoryRepository: CategoryRepository

    lateinit var categoryAPI: CategoryAPI

    @Before
    fun setup() {
        categoryAPI = mockk()
        categoryRepository = CategoryRepository(categoryAPI)
    }

    @Test
    fun `fetch topics or interests`() {
        coroutinesRule.runBlocking {
            coEvery { categoryAPI.getSubCategories(any()) } returns TestData.subCategoryResponse
            Assert.assertEquals(Result.Success(TestData.subCategoryResponse), categoryRepository.getTopics(null))
        }
    }

    @Test
    fun `throws IOException while fetching topics or interests`() {
        val ioException = IOException()
        coroutinesRule.runBlocking {
            coEvery { categoryAPI.getSubCategories(any()) } throws ioException
            Assert.assertEquals(Result.Failure(ioException), categoryRepository.getTopics(null))
        }
    }
}