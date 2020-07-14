package dev.nikhi1.recipe.onboarding.ui

import dev.nikhi1.recipe.onboarding.ui.model.Category
import dev.nikhi1.recipe.onboarding.ui.model.CategoryViewType

class CategoryPresenter {
    fun map(categories: List<Category>): List<CategoryViewType> = categories.map { CategoryViewType(it) }
}