package dev.nikhi1.eventbrite.onboarding

import dev.nikhi1.eventbrite.core.Result

interface DataRepository {
    suspend fun getTopics(): Result<List<String>>
}