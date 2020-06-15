package com.example.restopass.main.common.restaurant.restaurantsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.domain.MembershipType
import com.example.restopass.domain.MembershipsViewModel
import com.example.restopass.domain.Restaurant
import com.example.restopass.domain.RestaurantViewModel
import kotlinx.android.synthetic.main.fragment_restaurants_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantsListFragment : Fragment(), RestaurantAdapterListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var viewModel: MembershipsViewModel
    private lateinit var restaurantViewModel: RestaurantViewModel

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurants_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)
        restaurantViewModel = ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        val membershipId = arguments?.get("membershipId") as MembershipType
        val selectedMembership = viewModel.memberships.firstOrNull{
            membershipId == it.membershipId
        } ?: viewModel.actualMembership


        restaurantAdapter =
            RestaurantAdapter(
                this
            )
        restaurantAdapter.restaurants = selectedMembership!!.restaurants!!
        restaurantAdapter.membershipName = selectedMembership.name
        restaurantAdapter.notifyDataSetChanged()

        recyclerView = restaurantRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = restaurantAdapter
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    floatingButtton.hide()
                else if (dy < 0)
                    floatingButtton.show()
            }
        })
    }

    override fun onClick(restaurant: Restaurant) {
        restaurantViewModel.restaurant = restaurant

        coroutineScope.launch {
//            try {
//                membershipsViewModel.get()
//
//                membershipAdapter.memberships = formatMembershipList(membershipsViewModel)
//                membershipAdapter.notifyDataSetChanged()
//                loader.visibility = View.GONE
//                membershipRecycler.visibility = View.VISIBLE
//            } catch (e: Exception) {
//                if(isActive) {
//                    Timber.e(e)
//                    loader.visibility = View.GONE
//
//                    val titleView: View =
//                        layoutInflater.inflate(R.layout.alert_dialog_title, container, false)
//                    AlertDialog.getAlertDialog(
//                        context,
//                        titleView,
//                        view
//                    ).show()
//                }
//            }
        }
    }
}