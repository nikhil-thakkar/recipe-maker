package dev.nikhi1.recipe.onboarding.data.model

data class Category(
    val id: String,
    val name: String,
    val name_localized: String,
    val resource_uri: String,
    val short_name: String,
    val short_name_localized: String
)