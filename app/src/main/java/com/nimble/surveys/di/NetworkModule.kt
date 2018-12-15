package com.nimble.surveys.di

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
    const val SERVER_URL = "http://192.168.1.101:8080/api/"
//    const val SERVER_URL = "http://192.168.1.100:8080/api/"
//    const val SERVER_URL = "https://a50a57d4-6c31-4dfc-9c38-777818dbe01d.mock.pstmn.io/api/"
}

val networkModule = module {
    // OkHttp Bean for Crackphant Api
    single { createOkHttpClient() }

    // CrackphantApi Bean
//    single { createWebService<CrackphantApi>(get(), get()) }
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor).build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, moshi: Moshi): T {
    return Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build().create(T::class.java)
}