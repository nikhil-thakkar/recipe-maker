package dev.nikhi1.recipe.onboarding.data.model

data class Pagination(
    val continuation: String,
    val has_more_items: Boolean,
    val object_count: Int,
    val page_count: Int,
    val page_number: Int,
    val page_size: Int
) {
    companion object {
        val EMPTY = Pagination("", false, 0, 0, 0, 0)
    }
}