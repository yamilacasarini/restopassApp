package com.example.restopass.main.ui.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.Restaurant
import com.example.restopass.domain.RestaurantViewModel
import com.example.restopass.main.common.LocationService
import com.example.restopass.service.RestaurantService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber


class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var mapViewModel: MapViewModel
    private lateinit var restaurantModelView: RestaurantViewModel

    private lateinit var mMap: GoogleMap
    private var location: LatLng? = null
    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)
    private var currentRestaurants: List<Restaurant> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mapViewModel =
            ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        restaurantModelView =
            ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        fetchFilters(mapViewModel)
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchHereButton.visibility = View.GONE
        searchHereButton.setOnClickListener {
            search(mMap.cameraPosition.target)
            searchHereButton.visibility = View.GONE
        }
        hidePreview()
        val mapFragment =  childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapSearch.setEndIconOnClickListener {
            view.findNavController().navigate(R.id.filterFragment)
        }

        restaurantPreview.setOnClickListener {
            view.findNavController().navigate(R.id.restaurantFragment)
        }

        mapSearchEdit.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //mapViewModel.selectedFilters = mapViewModel.selectedFilters.copy(search = v.text.toString())
                search(mMap.cameraPosition.target)
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()
        location = null
        @SuppressLint("MissingPermission")
        if(LocationService.isLocationGranted())
            mMap.isMyLocationEnabled = true
        mMap.setOnMarkerClickListener {
            val restaurant = currentRestaurants.find { resto -> resto.name == it.title }
            restaurant?.let { fillRestaurantPreview(it) }
            true
        }
        mMap.setOnCameraMoveListener { searchHereButton.visibility = View.VISIBLE }
        positionMyLocationOnBottomRight()

        LocationService.addLocationListener {lastLocation : Location? ->
            lastLocation?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                location?:apply {
                    search(latLng, false)
                }
                location = latLng
            }

        }
    }

    private fun search(latLng: LatLng, moveToFirst: Boolean = true) {
        mMap.clear()
        moveCamera(latLng)
        mapViewModel.selectedFilters = mapViewModel.selectedFilters.copy(search = getFreeText())
        getRestaurantsForTags(latLng, mapViewModel.selectedFilters, moveToFirst)
        searchHereButton.visibility = View.GONE
    }

    private fun positionMyLocationOnBottomRight() {
        val locationButton= this.activity?.let { (it.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2")) }
        val rlp = locationButton?.layoutParams as (RelativeLayout.LayoutParams)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP,0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE)
        rlp.setMargins(0,0,30,30);
    }

    private fun getRestaurantsForTags(latLng: LatLng, selectedFilters: SelectedFilters, moveToFirst: Boolean) {
        coroutineScope.launch {
            try {
                val restaurants = RestaurantService.getRestaurantsForTags(latLng, selectedFilters)
                onRestaurantsSearched(restaurants, moveToFirst)
            } catch (e: Exception) {
                Timber.i("Error while getting restaurants for tags: ${selectedFilters}. Err: ${e.message}")
            }
        }
    }

    private fun onRestaurantsSearched(restaurants: List<Restaurant>, moveToFirst: Boolean) {
        Timber.i("Got ${restaurants.size} restaurants")
        currentRestaurants = restaurants
        Timber.i("Restaurants $restaurants")
        if(restaurants.isNotEmpty()) {
            restaurants.forEach {
                val position = LatLng(it.location.y, it.location.x)
                val marker = mMap.addMarker(MarkerOptions().position(position).title(it.name))
            }
            hidePreview()
            if(moveToFirst) {
                moveCamera(LatLng(restaurants[0].location.y, restaurants[0].location.x), 14f)
                fillRestaurantPreview(restaurants[0])
            }
        } else {
           hidePreview()
        }
    }

    private fun hidePreview() {
        restaurantPreview.visibility = View.GONE
        restoAvailable.visibility = View.GONE
        restoNotAvailable.visibility = View.GONE
    }

    private fun moveCamera(loc: LatLng, zoom: Float = 13f) = mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom))

    private fun fetchFilters(mapViewModel: MapViewModel) {
        coroutineScope.launch {
            try {
                val tags = RestaurantService.getTags()
                mapViewModel.filters = Filters(tags.tags, tags.memberships)
            } catch (e: Exception) {
                Timber.i("Error while getting tags: ${e.message}")
            }
        }
    }

    private fun fillRestaurantPreview(restaurant: Restaurant) {
        restaurantModelView.restaurant = restaurant
        restaurantPreview.visibility = View.GONE
        Glide.with(this).load(restaurant.img).into(restoImage)
        restoName.text = restaurant.name

        restaurantRating.rating = restaurant.stars


        AppPreferences.user.actualMembership?.let {
            if(isRestaurantInMembership(it, restaurant)) {
                restoAvailable.visibility = View.VISIBLE
                restoNotAvailable.visibility = View.GONE
            } else {
                showNotIncluded(restaurant)
            }
        } ?: showNotIncluded(restaurant)

        restaurantPreview.visibility = View.VISIBLE
    }

    private fun getFreeText(): String {
        val text = mapSearchEdit.text.toString()
        Timber.i("Written text: $text")
        return text
    }

    private fun showNotIncluded(restaurant: Restaurant) {
        val minMem = minimumMembership(restaurant)
        restoNotAvailable.text = resources.getString(R.string.not_included, minMem)
        restoAvailable.visibility = View.GONE
        restoNotAvailable.visibility = View.VISIBLE
    }
    private fun isRestaurantInMembership(membershipId: Int, restaurant: Restaurant): Boolean {
        return restaurant.dishes.any {
            it.isIncluded(membershipId)
        }
    }

    private fun minimumMembership(restaurant: Restaurant): String {
        return restaurant.dishes.minBy { it.baseMembership }!!.baseMembershipName
    }
}
