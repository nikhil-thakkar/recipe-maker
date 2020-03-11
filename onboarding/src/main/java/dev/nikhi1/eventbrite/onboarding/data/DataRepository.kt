package dev.nikhi1.eventbrite.onboarding.data

import dev.nikhi1.eventbrite.core.Result
import dev.nikhi1.eventbrite.onboarding.data.model.SubCategoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DataRepository {
    suspend fun getTopics(continuation: String?): Result<SubCategoryResponse>
}

interface CategoryAPI {
    @GET("/v3/subcategories")
    suspend fun getSubCategories(@Query("continuation") continuation: String?): SubCategoryResponse
}

class CategoryRepository(private val categoryAPI: CategoryAPI) : DataRepository {

    override suspend fun getTopics(continuation: String?): Result<SubCategoryResponse> {
        return try {
            Result.Success(categoryAPI.getSubCategories(continuation))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}