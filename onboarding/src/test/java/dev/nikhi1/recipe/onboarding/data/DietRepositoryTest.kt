package dev.nikhi1.recipe.onboarding.data

import dev.nikhi1.recipe.core.Result
import dev.nikhi1.recipe.onboarding.TestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class DietRepositoryTest {

    lateinit var dietRepository: DietRepository
    lateinit var dietDataSource: DietDataSource

    @Before
    fun setup() {
        dietDataSource = mockk()
        dietRepository = DietRepositoryImpl(dietDataSource)
    }

    @Test
    fun `fetch topics or interests`() {
        runBlocking {
            coEvery { dietDataSource.getAvailableDiets() } returns TestData.dietResponse
            Assert.assertEquals(Result.Success(TestData.dietResponse.diets), dietRepository.getDiets())
        }
    }

    @Test
    fun `throws IOException while fetching topics or interests`() {
        val ioException = IOException()
        runBlocking {
            coEvery { dietDataSource.getAvailableDiets() } throws ioException
            Assert.assertEquals(Result.Failure(ioException), dietRepository.getDiets())
        }
    }
}