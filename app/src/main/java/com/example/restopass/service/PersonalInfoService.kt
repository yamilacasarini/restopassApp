package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.domain.PersonalInfo
import com.example.restopass.domain.PersonalInfoRequest
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import timber.log.Timber

object PersonalInfoService {
    private const val BASE_URL = "https://restopass.herokuapp.com"

    interface PersonalInfoApi {
        @GET("/users")
        fun get() : Deferred<Response<PersonalInfo>>

        @PATCH("/users")
        fun update(@Body personalInfo: PersonalInfoRequest) : Deferred<Response<Void>>
    }

    private var api: PersonalInfoApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, PersonalInfoApi::class.java)
    }

    suspend fun get(): PersonalInfo {
        delay(1000L)
        return PersonalInfo("juanito", "cabanas", "juanito@gmail.co", mutableListOf("unoSecundario@gmail.com", "otroSecundario@gmail.com"))
        val response = api.get().await()
        Timber.i("Executed GET. Response code was ${response.code()}")

        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun update(personalInfo: PersonalInfoRequest) {
        val response = api.update(personalInfo).await()
        Timber.i("Executed PATCH. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }

}