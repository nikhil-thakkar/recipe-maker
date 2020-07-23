package dev.nikhi1.recipe.onboarding.data

import dev.nikhi1.recipe.core.DataSource
import dev.nikhi1.recipe.core.Repository
import dev.nikhi1.recipe.core.Result
import dev.nikhi1.recipe.onboarding.data.model.Diet
import dev.nikhi1.recipe.onboarding.data.model.DietResponse
import retrofit2.http.GET

interface DietRepository : Repository {
    suspend fun getDiets(): Result<List<Diet>>
}

interface DietDataSource : DataSource {
    @GET("/diets.json")
    suspend fun getAvailableDiets(): DietResponse
}

class DietRepositoryImpl(
    private val dietDataSource: DietDataSource
) : DietRepository {

    override suspend fun getDiets(): Result<List<Diet>> = try {
        Result.Success(dietDataSource.getAvailableDiets().diets)
    } catch (ex: Exception) {
        Result.Failure(ex)
    }
}
