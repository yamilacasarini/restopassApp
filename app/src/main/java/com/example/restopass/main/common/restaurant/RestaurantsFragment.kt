package com.example.restopass.main.common.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.domain.MembershipType
import com.example.restopass.domain.Memberships
import com.example.restopass.domain.Restaurant
import kotlinx.android.synthetic.main.fragment_restaurants.*

class RestaurantsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private val args: RestaurantsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurants, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurantAdapter = RestaurantAdapter()
        restaurantAdapter.restaurants = args.membership.restaurants!!
        restaurantAdapter.notifyDataSetChanged()
        recyclerView = restaurantRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = restaurantAdapter
        }
    }
}