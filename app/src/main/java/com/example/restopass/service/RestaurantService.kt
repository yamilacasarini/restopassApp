package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.domain.Restaurant
import com.example.restopass.domain.Tags
import com.example.restopass.main.ui.map.SelectedFilters
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import timber.log.Timber


object RestaurantService {
    private var api: RestopassApi = RetrofitFactory.createClient(BASE_URL, RestopassApi::class.java)

    interface RestopassApi {
        @PUT("restaurants")
        fun getRestaurantForTagsAsync(@Body tags: TagsRequestBody):
                Deferred<Response<List<Restaurant>>>

        @GET("restaurants/tags")
        fun getRestaurantTagsAsync(): Deferred<Response<Tags>>

        @GET("restaurants/{id}")
        fun getRestaurant(@Path("id") id: String): Deferred<Response<Restaurant>>
    }

    suspend fun getRestaurants(latLng: LatLng): List<Restaurant> {
        val response = api.getRestaurantForTagsAsync(TagsRequestBody(lat = latLng.latitude, lng = latLng.longitude)).await()
        Timber.i("Executed GET to ${response.raw()}. Response code was ${response.code()}")
        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun getRestaurantsForTags(latLng: LatLng, selectedFilters: SelectedFilters): List<Restaurant> {
        val response = api.getRestaurantForTagsAsync(TagsRequestBody(selectedFilters.search, selectedFilters.tags, selectedFilters.plan, latLng.latitude, latLng.longitude)).await()
        Timber.i("Executed GET to ${response.raw()}. Response code was ${response.code()}")
        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun getTags(): Tags {
        val response = api.getRestaurantTagsAsync().await()
        Timber.i("Executed GET to ${response.raw()}. Response code was ${response.code()}")
        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun getRestaurant(id: String): Restaurant {
        val response = api.getRestaurant(id).await()
        Timber.i("Executed GET to ${response.raw()}. Response code was ${response.code()}")
        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }
}

data class TagsRequestBody(val freeText: String? = null, val tags: List<String> = listOf(), val topMembership: Int? = null, val lat: Double, val lng: Double)
