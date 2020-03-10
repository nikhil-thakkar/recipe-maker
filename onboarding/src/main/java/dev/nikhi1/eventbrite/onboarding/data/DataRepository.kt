package dev.nikhi1.eventbrite.onboarding.data

import dev.nikhi1.eventbrite.core.Result
import retrofit2.http.GET

interface DataRepository {
    suspend fun getTopics(): Result<List<String>>
}

interface CategoryAPI {
    @GET
    suspend fun getCategories(): List<String>
}

class CategoryRepository(private val categoryAPI: CategoryAPI) : DataRepository {

    override suspend fun getTopics(): Result<List<String>> {
        return try {
            Result.Success(categoryAPI.getCategories())
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}