package dev.nikhi1.recipe.onboarding

import com.google.gson.Gson
import dev.nikhi1.recipe.onboarding.data.model.DietResponse
import dev.nikhi1.recipe.onboarding.ui.model.DietViewType

object TestData {

    private val gson = Gson()

    private val dietJson by lazy {
        getTextInFile("diets.json")
    }

    val dietResponse: DietResponse by lazy {
        gson.fromJson(dietJson, DietResponse::class.java)
    }

    val dietViewTypes: List<DietViewType> by lazy {
        dietResponse.diets.map { DietViewType(it) }
    }

    private fun getTextInFile(fileName: String): String? =
        ClassLoader.getSystemResourceAsStream(fileName)?.bufferedReader().use { it?.readText() }
}