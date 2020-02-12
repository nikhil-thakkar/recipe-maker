package dev.nikhi1.eventbrite.core

sealed class Result<out T : Any>

data class Success<out T : Any>(val data: T) : Result<T>()

data class Failure(val error: Throwable?) : Result<Nothing>()

object Loading: Result<Nothing>()
