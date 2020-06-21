package com.example.restopass.main.common.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.common.orElse
import com.example.restopass.domain.*
import com.example.restopass.service.RestaurantService
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.android.synthetic.main.fragment_restaurants_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class RestaurantRatingFragment : Fragment() {

    private lateinit var dishRecyclerView: RecyclerView
    private lateinit var dishAdapter: DishAdapterRating

    private lateinit var viewModel: MembershipsViewModel
    private lateinit var restaurantViewModel: RestaurantViewModel

    private lateinit var selectedMembership: SelectedMembershipViewModel

    private lateinit var restaurant: Restaurant

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rating_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurantScrollView.visibility = View.GONE
        loader.visibility = View.VISIBLE

        viewModel = ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)
        restaurantViewModel =
            ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        selectedMembership =
            ViewModelProvider(requireActivity()).get(SelectedMembershipViewModel::class.java)

        getRestaurant("b200dcd7-dabd-4df2-9305-edaf90dad56b")

    }

    private fun fillView(restaurant: Restaurant) {

        restaurant.let {
            Glide.with(this).load(it.img).into(restaurantImage)
            restaurantName.text = it.name
            restaurantAddress.text = it.address
        }

        AppPreferences.user.actualMembership?.let {
            val filteredDishes = restaurant.dishes.filter {dish ->
                dish.isIncluded(it)
            }
            dishAdapter = DishAdapterRating(filteredDishes)
        }

        dishAdapter.notifyDataSetChanged()
        dishRecyclerView = dishRecyclerV.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = dishAdapter
        }

    }

    private fun getRestaurant(id: String) {
        coroutineScope.launch {
            try {
                restaurant = RestaurantService.getRestaurant(id)
                fillView(restaurant)
                restaurantScrollView.visibility = View.VISIBLE
                loader.visibility = View.GONE
            } catch (e: Exception) {
                Timber.i("Error while getting restaurant for id ${id}. Err: ${e.message}")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}