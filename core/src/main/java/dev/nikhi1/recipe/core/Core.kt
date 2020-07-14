package dev.nikhi1.recipe.core

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val <T> T.exhaustive: T
    get() = this

object Core {

    operator fun invoke(debuggable: Boolean) {
        loadKoinModules(module {
            single { provideRetrofit(getProperty("BASE_URL"), get()) }
            factory { provideOkHttpClient(get(), get()) }
            factory { provideApiKeyInterceptor(getProperty("EVENTBRITE_API_KEY")) }
            factory { provideLoggingHttpInterceptor(debuggable) }
        })
    }

    private fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    private fun provideLoggingHttpInterceptor(debuggable: Boolean): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (debuggable) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    private fun provideApiKeyInterceptor(apiKey: String): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder().addHeader("Authorization", "Bearer $apiKey")
            chain.proceed(newRequest.build())
        }
    }

    private fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(apiKeyInterceptor)
        }.build()
    }

}