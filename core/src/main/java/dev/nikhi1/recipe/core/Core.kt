package dev.nikhi1.recipe.core

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

val <T> T.exhaustive: T
    get() = this

object Core {

    operator fun invoke(debuggable: Boolean) {
        loadKoinModules(module {
            single { provideRetrofit(getProperty("BASE_URL"), get()) }
            factory { provideOkHttpClient(get(), get(), get(named("api"))) }
            factory { provideApiKeyInterceptor(getProperty("API_KEY")) }
            factory { provideLoggingHttpInterceptor(debuggable) }
            factory(named("api")) { provideLocalJsonAPIRequestInterceptor(get()) }
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
            val newUrl = request.url.newBuilder().addQueryParameter("apiKey", apiKey).build()
            chain.proceed(request.newBuilder().url(newUrl).build())
        }
    }

    private fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: Interceptor,
        localJsonAPIRequestInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(apiKeyInterceptor)
            addInterceptor(localJsonAPIRequestInterceptor)
        }.build()
    }

    private fun provideLocalJsonAPIRequestInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            if (request.url.pathSegments.last().endsWith(".json")) {
                try {
                    val contents = context.readAssetFile(request.url.pathSegments.last())
                    return@Interceptor Response.Builder().protocol(Protocol.HTTP_1_1).request(request).code(200)
                        .message("")
                        .body(contents.toByteArray().toResponseBody("application/json".toMediaType())).build()
                } catch (ex: IOException) {
                    return@Interceptor Response.Builder().protocol(Protocol.HTTP_1_1).request(request).code(404)
                        .message("").build()
                }
            }
            chain.proceed(request)
        }
    }
}