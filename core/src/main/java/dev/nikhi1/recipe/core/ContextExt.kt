package dev.nikhi1.recipe.core

import android.content.Context
import java.io.IOException

@Throws(IOException::class)
fun Context.readAssetFile(fileName: String): String = assets.open(fileName).bufferedReader().use {
    it.readText()
}