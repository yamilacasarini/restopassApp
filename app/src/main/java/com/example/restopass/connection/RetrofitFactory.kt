package com.example.restopass.connection

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    fun <T> createClient(baseUrl: String, clazz: Class<T>, uow: String? = null): T {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()


        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(ObjectMapperProvider.mapper))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(clazz)
    }
}