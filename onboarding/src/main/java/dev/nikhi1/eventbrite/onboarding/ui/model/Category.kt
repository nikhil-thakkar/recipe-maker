package dev.nikhi1.eventbrite.onboarding.ui.model

import dev.nikhi1.eventbrite.core.ViewType
import dev.nikhi1.eventbrite.onboarding.R

data class Category(
    val id: String,
    val name: String,
    val subcategories: List<Subcategory>
)

data class Subcategory(
    val id: String,
    val name: String)

data class CategoryViewType(private val data: Category): ViewType<Category> {

    override fun layoutId(): Int = R.layout.item_category

    override fun data(): Category = data
}