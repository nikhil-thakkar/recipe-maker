package dev.nikhi1.recipe.onboarding.ui.model

import dev.nikhi1.recipe.core.ViewType
import dev.nikhi1.recipe.onboarding.R
import dev.nikhi1.recipe.onboarding.data.model.Diet

data class DietViewType(private val data: Diet) : ViewType<Diet> {

    override fun layoutId(): Int = R.layout.item_category

    override fun data(): Diet = data
}