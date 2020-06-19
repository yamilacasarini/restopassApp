package com.example.restopass.main.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.common.orElse
import com.example.restopass.domain.*
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.main.common.LocationService
import com.example.restopass.main.common.membership.MembershipAdapter
import com.example.restopass.main.common.membership.MembershipAdapterListener
import com.example.restopass.main.common.restaurant.restaurantsList.RestaurantAdapter
import com.example.restopass.main.common.restaurant.restaurantsList.RestaurantAdapterListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import timber.log.Timber

class HomeFragment : Fragment(), RestaurantAdapterListener, MembershipAdapterListener {
    private lateinit var membershipRecyclerView: RecyclerView
    private lateinit var membershipAdapter: MembershipAdapter

    private lateinit var restaurantRecyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantViewModel: RestaurantViewModel

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var membershipsViewModel: MembershipsViewModel

    private lateinit var selectedMembership: SelectedMembershipViewModel

    private val fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
    private val permissions = arrayOf(fineLocation, coarseLocation)
    private val permissionCode = 1234
    private var locationGranted = false
    private lateinit var location: LatLng

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        membershipsViewModel =
            ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)

        restaurantViewModel =
            ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        membershipAdapter = MembershipAdapter(this)
        membershipRecyclerView = homeMembershipRecycler.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = membershipAdapter
        }

        restaurantAdapter =
            RestaurantAdapter(
                this
            )
        restaurantRecyclerView = homeRestaurantRecycler.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = restaurantAdapter
        }
    }

    override fun onStart() {
        super.onStart()

        if (job.isCancelled) {
            job = Job()
            coroutineScope = CoroutineScope(job + Dispatchers.Main)
        }

        initializeLocation()
        loader.visibility = View.VISIBLE
        AppPreferences.user.actualMembership?.let {
            //TODO: Home de usuario con membresía
            coroutineScope.launch {
                val deferred = mutableListOf(getMemberships())
                if (LocationService.isLocationGranted()) {
                    deferred.add(getRestaurants())
                }
                deferred.awaitAll()

                loader.visibility = View.GONE
            }
        }.orElse {
            coroutineScope.launch {
                val deferred = listOf(getMemberships(), getRestaurants())
                deferred.awaitAll()

                loader.visibility = View.GONE
            }

        }

    }


    private fun getMemberships(): Deferred<Unit> {
        return coroutineScope.async {
            try {
                membershipsViewModel.get()

                membershipAdapter.memberships = membershipsViewModel.memberships
                membershipAdapter.notifyDataSetChanged()

                membershipSection.visibility = View.VISIBLE
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    view?.findNavController()?.navigate(R.id.refreshErrorFragment)
                }
            }
        }
    }

    private fun getRestaurants(): Deferred<Unit> {
        return coroutineScope.async {
            LocationService.addLocationListener { lastLocation: Location? ->
                coroutineScope.launch {
                    try {
                        homeViewModel.getRestaurants(LatLng(lastLocation!!.latitude, lastLocation.longitude))

                        restaurantAdapter.restaurants = homeViewModel.restaurants
                        restaurantAdapter.notifyDataSetChanged()

                        homeRestaurantRecycler.visibility = View.VISIBLE
                        restaurantSection.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        if (isActive) {
                            Timber.e(e)
                            view?.findNavController()?.navigate(R.id.refreshErrorFragment)
                        }
                    }
                }
            }
        }
    }

    private fun initializeLocation() {
        locationGranted = getLocationPermissions()
        if (!locationGranted)
            requestLocationPermission()
        else
            getLocation()
    }

    private fun getLocation() {
        val fuseLoc = this.context?.let { LocationServices.getFusedLocationProviderClient(it) }
        if (locationGranted) {
            fuseLoc?.lastLocation?.addOnSuccessListener { lastLocation: Location? ->
                lastLocation?.let {
                    location = LatLng(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun getLocationPermissions() = permissions.all { perm ->
        this.context?.let {
            ContextCompat.checkSelfPermission(
                it,
                perm
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() =
        this.activity?.let { ActivityCompat.requestPermissions(it, permissions, permissionCode) }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                locationGranted = true
                getLocation()
            }
        }
    }

    override suspend fun onClick(restaurant: Restaurant) {
        withContext(coroutineScope.coroutineContext) {
            try {
                loader.visibility = View.VISIBLE
                restaurantViewModel.get(restaurant.restaurantId)

            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    loader.visibility = View.GONE

                    val titleView: View =
                        layoutInflater.inflate(R.layout.alert_dialog_title, container, false)
                    AlertDialog.getAlertDialog(
                        context,
                        titleView,
                        view
                    ).show()
                }
            }
        }

        findNavController().navigate(R.id.restaurantFragment)
    }


    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    override fun onClick(membership: Membership) {
        selectedMembership = ViewModelProvider(requireActivity()).get(SelectedMembershipViewModel::class.java)
        selectedMembership.membership = membership
    }


}
