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
import com.example.restopass.common.orElse
import com.example.restopass.domain.MembershipsViewModel
import com.example.restopass.domain.RestaurantViewModel
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.android.synthetic.main.view_restaurant_item.view.*

class RestaurantFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tagAdapter: TagAdapter
    private lateinit var viewModel: MembershipsViewModel
    private lateinit var restaurantViewModel: RestaurantViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val membershipName = arguments?.getString("membershipName")

        viewModel = ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)
        restaurantViewModel = ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        val restaurant = restaurantViewModel.restaurant


        tagAdapter = TagAdapter()

        tagAdapter.tags = restaurant.tags
        tagAdapter.notifyDataSetChanged()

        recyclerView = tagRestaurantRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = tagAdapter
        }

        restaurant.let {
            Glide.with(this).load(it.img).into(restaurantImage)
            restaurantName.text = it.name
            restaurantAddress.text = it.address
        }

        val stars = restaurant.stars
        repeat(stars.toInt()) { index ->
            val starId =
                resources.getIdentifier("star${index + 1}", "id", requireContext().packageName)
            view.findViewById<View>(starId).visibility = View.VISIBLE
        }
        val hasHalfStar = stars.minus(stars.toInt()) == 0.5
        if (hasHalfStar) halfStar.visibility = View.VISIBLE

        viewModel.actualMembership?.let {
            if (it.restaurants!!.any { aRestaurant ->  aRestaurant.restaurantId == restaurant.restaurantId})
                floatingButton.setText(R.string.bookTable)
            else {
                setButtonByMembership(membershipName)
            }
        }.orElse {
            setButtonByMembership(membershipName)
        }


    }

    private fun setButtonByMembership(membershipName: String?) {
        membershipName?.let {
            val chooseMembership = resources.getString(R.string.chooseMembership, membershipName)
            floatingButton.text = chooseMembership
        }.orElse {
            floatingButton.setText(R.string.showMemberships)
        }
    }
}