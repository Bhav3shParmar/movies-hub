package com.app.mvvmdemo.apidata

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val retrofit2 by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: ApiResponse by lazy {
        retrofit.create(ApiResponse::class.java)
    }

    val service2 :ApiResponse by lazy {
        retrofit2.create(ApiResponse::class.java)
    }

}