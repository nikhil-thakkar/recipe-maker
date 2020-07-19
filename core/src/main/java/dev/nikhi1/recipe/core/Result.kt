package dev.nikhi1.recipe.core

sealed class Result<out T> {
    
    data class Success<out T : Any>(val data: T?) : Result<T>()

    data class Failure(val error: Throwable?) : Result<Nothing>()
}


