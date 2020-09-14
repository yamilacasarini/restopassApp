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
import com.example.restopass.main.common.AlertBody
import com.example.restopass.main.common.LocationService
import com.example.restopass.service.RestaurantService
import com.example.restopass.utils.AlertDialogUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_communications.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.math.pow
import kotlin.math.sqrt


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapViewModel: MapViewModel
    private lateinit var restaurantModelView: RestaurantViewModel

    private lateinit var mMap: GoogleMap
    private var location: LatLng? = null

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)
    var firstInitialized: Boolean = true

    private var currentRestaurants: List<Restaurant> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapViewModel =
            ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        restaurantModelView =
            ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        fetchFilters()

        searchHereButton.apply {
            setOnClickListener {
                search(mMap.cameraPosition.target)
                searchHereButton.visibility = View.GONE
            }

            visibility = View.GONE
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapSearch.setEndIconOnClickListener {
            view.findNavController().navigate(R.id.filterFragment)
        }

        restaurantPreview.setOnClickListener {
            view.findNavController().navigate(R.id.restaurantFragment)
        }

        mapSearchEdit.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
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
        if (LocationService.isLocationGranted())
            mMap.isMyLocationEnabled = true
        mMap.setOnMarkerClickListener {
            val restaurant = currentRestaurants.find { resto -> resto.name == it.title }
            restaurant?.let { fillRestaurantPreview(it) }
            true
        }
        mMap.setOnCameraMoveListener { searchHereButton.visibility = View.VISIBLE }
        positionMyLocationOnBottomRight()

        LocationService.addLocationListener { lastLocation: Location? ->
            lastLocation?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                location ?: apply {
                   if (firstInitialized) {
                       moveCamera(latLng)
                       firstInitialized = false
                   }

                    if (mapViewModel.selectedFilters.isAnyFilterSelected()) search(mMap.cameraPosition.target) else search(latLng)
                }
                location = latLng
            }
        }


    }

    private fun search(latLng: LatLng) {
        mMap.clear()

        mapViewModel.selectedFilters = mapViewModel.selectedFilters.copy(search = getFreeText())
        getRestaurantsForTags(latLng, getVisibleMapKmRadius(), mapViewModel.selectedFilters)
        searchHereButton.visibility = View.GONE
    }

    private fun getVisibleMapKmRadius(): Double {
        val visibleRegion = mMap.projection.visibleRegion

        val distanceWidth = FloatArray(1)
        val distanceHeight = FloatArray(1)

        val farRight = visibleRegion.farRight
        val farLeft = visibleRegion.farLeft
        val nearRight = visibleRegion.nearRight
        val nearLeft = visibleRegion.nearLeft

        Location.distanceBetween(
            (farLeft.latitude + nearLeft.latitude) / 2,
            farLeft.longitude,
            (farRight.latitude + nearRight.latitude) / 2,
            farRight.longitude, distanceWidth
        )

        Location.distanceBetween(
            farRight.latitude,
            (farRight.longitude + farLeft.longitude) / 2,
            nearRight.latitude,
            (nearRight.longitude + nearLeft.longitude) / 2,
            distanceHeight
        )

        return sqrt(
            (distanceWidth[0].toString().toDouble().pow(2.0))
                    + distanceHeight[0].toString().toDouble().pow(2.0)
        ) / (1000 * 2).toDouble()

    }

    private fun positionMyLocationOnBottomRight() {
        val locationButton = this.activity?.let {
            (it.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(
                Integer.parseInt(
                    "2"
                )
            )
        }
        val rlp = locationButton?.layoutParams as (RelativeLayout.LayoutParams)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 30, 30);
    }

    private fun getRestaurantsForTags(
        latLng: LatLng,
        radius: Double,
        selectedFilters: SelectedFilters
    ) {
        if (job.isCancelled) {
            job = Job()
            coroutineScope = CoroutineScope(job + Dispatchers.Main)
        }

        coroutineScope.launch {
            try {
                val restaurants =
                    RestaurantService.getRestaurantsForTags(latLng, radius, selectedFilters)
                onRestaurantsSearched(restaurants)
            } catch (e: Exception) {
                if (isActive) {
                    Timber.i("Error while getting restaurants for tags: ${selectedFilters}. Err: ${e.message}")
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, mapContainer, view).show()
                }
            }
        }
    }

    private fun onRestaurantsSearched(restaurants: List<Restaurant>) {
        Timber.i("Got ${restaurants.size} restaurants")
        currentRestaurants = restaurants
        Timber.i("Restaurants $restaurants")

        if (restaurants.isNotEmpty()) {
            restaurants.forEach {
                val position = LatLng(it.location.y, it.location.x)
                mMap.addMarker(MarkerOptions().position(position).title(it.name))
            }
        } else {
            AlertDialogUtils.buildAlertDialog(null, layoutInflater, mapContainer,
                alertBody = AlertBody(getString(R.string.restaurantsNotFoundTitle), getString(R.string.restaurantNotFoundDescription)))
                .show()
        }
        hidePreview()
    }

    private fun hidePreview() {
        restaurantPreview.visibility = View.GONE
        restoAvailable.visibility = View.GONE
        restoNotAvailable.visibility = View.GONE
    }

    private fun moveCamera(loc: LatLng, zoom: Float = 13f) =
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom))

    private fun fetchFilters() {
        coroutineScope.launch {
            try {
                mapViewModel.getFilters()
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
            if (isRestaurantInMembership(it, restaurant)) {
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

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapViewModel.selectedFilters.clear()
    }
}
