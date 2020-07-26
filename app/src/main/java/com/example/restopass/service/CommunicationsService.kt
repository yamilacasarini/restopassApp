package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.Path
import timber.log.Timber

object CommunicationsService {
    interface CommunicationsApi {
        @PATCH("/users/emails")
        fun add(@Body email: SecondaryEmailRequest) : Deferred<Response<Void>>

        @DELETE("/users/emails/{email}")
        fun delete(@Path("email") email: String) : Deferred<Response<Void>>
    }

    private var api: CommunicationsApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, CommunicationsApi::class.java)
    }

    suspend fun add(email: String) {
        val response = api.add(SecondaryEmailRequest(email)).await()
        Timber.i("Executed PATCH. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }

    suspend fun delete(email: String) {
        val response = api.delete(email).await()
        Timber.i("Executed DELETE. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }

    data class SecondaryEmailRequest(val secondaryEmail: String)

}