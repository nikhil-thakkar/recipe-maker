package dev.nikhi1.eventbrite.onboarding.data

import dev.nikhi1.eventbrite.core.Result
import dev.nikhi1.eventbrite.onboarding.data.model.SubCategoryResponse
import retrofit2.http.GET

interface DataRepository {
    suspend fun getTopics(): Result<SubCategoryResponse>
}

interface CategoryAPI {
    @GET("v3/subcategories")
    suspend fun getSubCategories(): SubCategoryResponse
}

class CategoryRepository(private val categoryAPI: CategoryAPI) : DataRepository {

    override suspend fun getTopics(): Result<SubCategoryResponse> {
        return try {
            Result.Success(categoryAPI.getSubCategories())
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}