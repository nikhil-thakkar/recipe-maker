package dev.nikhi1.recipe.onboarding.data

import dev.nikhi1.recipe.core.DataSource
import dev.nikhi1.recipe.core.Repository
import dev.nikhi1.recipe.core.Result
import dev.nikhi1.recipe.onboarding.data.model.Diet
import dev.nikhi1.recipe.onboarding.data.model.DietResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.GET

interface DietRepository : Repository {
    suspend fun getDiets(): Result<List<Diet>>
}

interface DietDataSource : DataSource {
    @GET("/diets.json")
    suspend fun getAvailableDiets(): DietResponse
}

class DietRepositoryImpl(private val dietDataSource: DietDataSource, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : DietRepository {

    override suspend fun getDiets(): Result<List<Diet>> = withContext(dispatcher) {
        try {
            Result.Success(dietDataSource.getAvailableDiets().diets)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}