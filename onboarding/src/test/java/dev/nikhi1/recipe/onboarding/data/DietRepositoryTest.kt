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
class DietRepositoryTest {

    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    lateinit var dietRepository: DietRepository

    lateinit var dietDataSource: DietDataSource

    @Before
    fun setup() {
        dietDataSource = mockk()
        dietRepository = DietRepositoryImpl(dietDataSource, coroutinesRule.testDispatcher)
    }

    @Test
    fun `fetch topics or interests`() {
        coroutinesRule.runBlocking {
            coEvery { dietDataSource.getAvailableDiets() } returns TestData.dietResponse
            Assert.assertEquals(Result.Success(TestData.dietResponse.diets), dietRepository.getDiets())
        }
    }

    @Test
    fun `throws IOException while fetching topics or interests`() {
        val ioException = IOException()
        coroutinesRule.runBlocking {
            coEvery { dietDataSource.getAvailableDiets() } throws ioException
            Assert.assertEquals(Result.Failure(ioException), dietRepository.getDiets())
        }
    }
}