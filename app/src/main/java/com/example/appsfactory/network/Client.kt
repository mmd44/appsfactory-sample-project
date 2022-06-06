package com.example.appsfactory.network

import com.example.appsfactory.features.lastfm.api.LastFMService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val logging = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

private val tokenInterceptor = Interceptor { chain ->
    val original = chain.request()
    val httpUrl = original.url
    val newHttpUrl = httpUrl
        .newBuilder()
        .addQueryParameter("api_key", "2153f4a542360f9512adf2ef2106269a")
        .build()
    val requestBuilder = original
        .newBuilder()
        .url(newHttpUrl)
    val request = requestBuilder
        .build()
    chain.proceed(request)
}

private val okHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(
        logging
    )
    addInterceptor(
        tokenInterceptor
    )
}.build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(okHttpClient)
    .baseUrl(APIs.BASE_URL)
    .build()

object LastFM {
    val lastFMService: LastFMService by lazy { retrofit.create(LastFMService::class.java) }
}
