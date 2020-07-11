package com.example.restopass.main.common.restaurant.restaurantsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.*
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.utils.AlertDialogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_restaurants_list.*
import kotlinx.coroutines.*
import timber.log.Timber

class RestaurantsListFragment : Fragment(), RestaurantAdapterListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var membershipsViewModel: MembershipsViewModel
    private lateinit var restaurantViewModel: RestaurantViewModel

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurants_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membershipsViewModel =
            ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)
        restaurantViewModel =
            ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        restaurantAdapter = RestaurantAdapter(this)
        restaurantAdapter.restaurants = membershipsViewModel.selectedMembership?.restaurants!!
        restaurantAdapter.notifyDataSetChanged()

        recyclerView = restaurantRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = restaurantAdapter
        }

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = resources.getString(R.string.restaurantsListToolbarTitle, membershipsViewModel.selectedMembership!!.name)
            show()
        }


        if (AppPreferences.user.actualMembership == membershipsViewModel.selectedMembership!!.membershipId) {
            selectMembershipButton.visibility = View.GONE
        } else {
            selectMembershipButton.setOnClickListener {
                toggleLoader()
                coroutineScope.launch {
                    try {
                        membershipsViewModel.update(membershipsViewModel.selectedMembership!!)
                        findNavController().navigate(R.id.navigation_enrolled_home)
                    } catch (e: Exception) {
                        if (isActive) {
                            Timber.e(e)
                            toggleLoader()
                            AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                        }
                    }
                }
            }
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0)
                        selectMembershipButton.hide()
                    else if (dy < 0)
                        selectMembershipButton.show()
                }
            })
        }
    }


    override fun onStart() {
        super.onStart()
        if (job.isCancelled) {
            job = Job()
            coroutineScope = CoroutineScope(job + Dispatchers.Main)
        }
    }

    private fun toggleLoader() {
        restaurantsListLoader.visibility =
            if (restaurantsListLoader.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        restaurantsList.visibility =
            if (restaurantsListLoader.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    override suspend fun onClick(restaurant: Restaurant) {
        (activity as AppCompatActivity).supportActionBar?.hide()
        withContext(coroutineScope.coroutineContext) {
            try {
                restaurantsList.visibility = View.GONE
                restaurantsListLoader.visibility = View.VISIBLE
                restaurantViewModel.get(restaurant.restaurantId)
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    restaurantsListLoader.visibility = View.GONE
                    restaurantsList.visibility = View.VISIBLE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                }
            }
        }


        findNavController().navigate(
            R.id.restaurantFragment,
            bundleOf("isMembershipSelected" to true)
        )
    }


    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}