package dev.nikhi1.recipe.onboarding.data.model

data class DietResponse(val diets: List<Diet>) {

    companion object {
        val EMPTY = DietResponse(emptyList())
    }
}
