package com.nimble.surveys.di

import com.nimble.surveys.api.SurveysApi
import com.nimble.surveys.di.NetworkProperties.SERVER_URL
import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object NetworkProperties {
    const val SERVER_URL = "https://nimbl3-survey-api.herokuapp.com/"
}

val networkModule = module {
    single { createOkHttpClient() }
    single { createWebService<SurveysApi>(get(), get()) }
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor {
            val original = it.request()
            val request = original.newBuilder()
                .header("Content-type", "application/json")
                .method(original.method(), original.body())
                .build()

            it.proceed(request)
        }
        .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, moshi: Moshi): T {
    return Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build().create(T::class.java)
}