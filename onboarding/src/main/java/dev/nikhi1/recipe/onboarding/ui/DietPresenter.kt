package dev.nikhi1.recipe.onboarding.ui

import dev.nikhi1.recipe.onboarding.data.model.Diet
import dev.nikhi1.recipe.onboarding.ui.model.DietViewType

class DietPresenter {
    fun map(diets: List<Diet>): List<DietViewType> = diets.map { DietViewType(it) }
}