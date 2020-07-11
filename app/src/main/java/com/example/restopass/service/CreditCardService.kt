package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.domain.CreditCard
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import timber.log.Timber

object CreditCardService  {
    private var api: RestopassApi = RetrofitFactory.createClient(BASE_URL, RestopassApi::class.java)

    interface RestopassApi {
        @POST("/credit-card")
        fun insertAsync(@Body creditCard: CreditCard): Deferred<Response<Void>>

        @DELETE("/credit-card")
        fun deleteAsync(): Deferred<Response<Void>>
    }

    suspend fun insert(creditCard: CreditCard) {
        val response = api.insertAsync(creditCard).await()
        Timber.i("Executed POST. Response code was ${response.code()}")
        if (!response.isSuccessful) throw response.error()
    }

    suspend fun delete() {
        val response = api.deleteAsync().await()
        Timber.i("Executed POST. Response code was ${response.code()}")
        if (!response.isSuccessful) throw response.error()
    }
}