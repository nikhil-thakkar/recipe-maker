package dev.nikhi1.eventbrite.onboarding.ui.model

data class Category(
    val id: String,
    val name: String,
    val subcategories: List<Subcategory>
)

data class Subcategory(
    val id: String,
    val name: String)