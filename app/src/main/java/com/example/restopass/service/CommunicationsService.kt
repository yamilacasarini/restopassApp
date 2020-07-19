package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import timber.log.Timber

object CommunicationsService {
    private const val BASE_URL = "https://restopass.herokuapp.com"

    interface CommunicationsApi {
        @PATCH("/users/emails")
        fun add(@Body email: SecondaryEmailRequest) : Deferred<Response<Void>>

        @DELETE("/users/emails")
        fun delete(@Body email: SecondaryEmailRequest) : Deferred<Response<Void>>
    }

    private var api: CommunicationsApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, CommunicationsApi::class.java)
    }

    suspend fun add(email: String) {
        delay(1000L)
        return
        val response = api.add(SecondaryEmailRequest(email)).await()
        Timber.i("Executed PATCH. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }

    suspend fun delete(email: String) {
        delay(1000L)
        return
        val response = api.delete(SecondaryEmailRequest(email)).await()
        Timber.i("Executed DELETE. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }

    data class SecondaryEmailRequest(val email: String)

}