package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.domain.CreditCard
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import timber.log.Timber

object PaymentService  {
    private var api: RestopassApi = RetrofitFactory.createClient(BASE_URL, RestopassApi::class.java)

    interface RestopassApi {
        @GET("/users/payment")
        fun getAsync(): Deferred<Response<CreditCard>>

        @PATCH("/users/payment")
        fun insertAsync(@Body creditCard: CreditCard): Deferred<Response<Void>>

        @DELETE("/users/payment")
        fun deleteAsync(): Deferred<Response<Void>>
    }

    suspend fun get(): CreditCard? {
        val response = api.getAsync().await()
        Timber.i("Executed GET. Response code was ${response.code()}")
        return when {
            response.isSuccessful -> response.body()
            else -> throw response.error()
        }
    }

    suspend fun insert(creditCard: CreditCard) {
        val response = api.insertAsync(creditCard).await()
        Timber.i("Executed PATH. Response code was ${response.code()}")
        if (!response.isSuccessful) throw response.error()
    }

    suspend fun delete() {
        val response = api.deleteAsync().await()
        Timber.i("Executed POST. Response code was ${response.code()}")
        if (!response.isSuccessful) throw response.error()
    }
}