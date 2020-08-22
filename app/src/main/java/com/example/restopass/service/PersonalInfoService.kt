package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.domain.PersonalInfo
import com.example.restopass.domain.PersonalInfoRequest
import com.example.restopass.domain.PersonalInfoResponse
import com.example.restopass.domain.SecondaryEmail
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import timber.log.Timber

object PersonalInfoService {
    interface PersonalInfoApi {
        @GET("/users")
        fun getAsync() : Deferred<Response<PersonalInfoResponse>>

        @DELETE("/users")
        fun deleteAccountAsync() : Deferred<Response<Void>>

        @PATCH("/users")
        fun updateAsync(@Body personalInfo: PersonalInfoRequest) : Deferred<Response<Void>>
    }

    private var api: PersonalInfoApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, PersonalInfoApi::class.java)
    }

    suspend fun get(): PersonalInfo {
        val response = api.getAsync().await()
        Timber.i("Executed GET. Response code was ${response.code()}")

        return when {
            response.isSuccessful -> response.body()!!.toClient()
            else -> throw response.error()
        }
    }

    suspend fun update(personalInfo: PersonalInfoRequest) {
        val response = api.updateAsync(personalInfo).await()
        Timber.i("Executed PATCH. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }

    suspend fun deleteAccount() {
        val response = api.deleteAccountAsync().await()
        Timber.i("Executed DELETE. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }

    private fun PersonalInfoResponse.toClient(): PersonalInfo {
        val confirmedEmails = this.secondaryEmails.map { SecondaryEmail(it) }.toMutableList()
        val toConfirmEmais = this.toConfirmEmails.map { SecondaryEmail(it, false) }

        val emails = confirmedEmails.apply {
            addAll(toConfirmEmais)
        }

        return PersonalInfo(this.name, this.lastName, this.email, emails, this.password)
    }

}