package dev.nikhi1.eventbrite.onboarding.data.model

data class Subcategory(
    val id: String,
    val name: String,
    val name_localized: String,
    val parent_category: ParentCategory,
    val resource_uri: String
)