package dev.nikhi1.recipe.onboarding.data.model

data class Subcategory(
    val id: String,
    val name: String,
    val name_localized: String,
    val parent_category: Category,
    val resource_uri: String
)