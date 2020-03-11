package dev.nikhi1.eventbrite.onboarding

import dev.nikhi1.eventbrite.onboarding.data.model.Pagination
import dev.nikhi1.eventbrite.onboarding.data.model.ParentCategory
import dev.nikhi1.eventbrite.onboarding.data.model.SubCategoryResponse
import dev.nikhi1.eventbrite.onboarding.data.model.Subcategory

object TestData {

    val category = ParentCategory(
        id = "101",
        name_localized = "Business & Professional",
        name = "Business & Professional",
        resource_uri = "https://www.eventbriteapi.com/v3/categories/101/",
        short_name = "Business",
        short_name_localized = "Business"
    )

    val subcategory = Subcategory(
        id = "1001",
        resource_uri = "https://www.eventbriteapi.com/v3/subcategories/1001/",
        name = "Startups & Small Business",
        name_localized = "Startups & Small Business",
        parent_category = category
    )

    val subCategoryResponse = SubCategoryResponse(Pagination.EMPTY, listOf(subcategory))

    val emptySubCategoryResponse = SubCategoryResponse.EMPTY
}