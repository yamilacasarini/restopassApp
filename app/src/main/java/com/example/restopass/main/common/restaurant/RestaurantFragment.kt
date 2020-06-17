package com.example.restopass.main.common.restaurant

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.orElse
import com.example.restopass.domain.MembershipsViewModel
import com.example.restopass.domain.Restaurant
import com.example.restopass.domain.RestaurantViewModel
import com.example.restopass.domain.SelectedMembershipViewModel
import kotlinx.android.synthetic.main.fragment_restaurant.*

class RestaurantFragment : Fragment() {
    private lateinit var tagRecyclerView: RecyclerView
    private lateinit var tagAdapter: TagAdapter

    private lateinit var dishRecyclerView: RecyclerView
    private lateinit var dishAdapter: DishAdapter

    private lateinit var viewModel: MembershipsViewModel
    private lateinit var restaurantViewModel: RestaurantViewModel

    private lateinit var selectedMembership: SelectedMembershipViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)
        restaurantViewModel =
            ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        selectedMembership =
            ViewModelProvider(requireActivity()).get(SelectedMembershipViewModel::class.java)

        val restaurant = restaurantViewModel.restaurant


        tagAdapter = TagAdapter()

        tagAdapter.tags = restaurant.tags
        tagAdapter.notifyDataSetChanged()

        tagRecyclerView = tagRestaurantRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = tagAdapter
        }

        val isMembershipSelected = arguments?.getBoolean("isMembershipSelected")

        isMembershipSelected?.let {
            selectedMembership.membership?.apply {
                val sortedRestaurants = restaurant.dishes.sortedBy {
                    it.isIncluded(this.membershipId!!)
                }
                dishAdapter = DishAdapter(sortedRestaurants)
            }
        }.orElse {
                dishAdapter = DishAdapter(restaurant.dishes)
        }


        dishAdapter.notifyDataSetChanged()
        dishRecyclerView = dishRecyclerV.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = dishAdapter
        }

        val selectedMembershipName =  isMembershipSelected?.run { selectedMembership.membership?.name }
        fillView(restaurant, selectedMembershipName)

    }

    private fun fillView(restaurant: Restaurant, membershipName: String?) {
        restaurant.let {
            Glide.with(this).load(it.img).into(restaurantImage)
            restaurantName.text = it.name
            restaurantAddress.text = it.address
        }

        val stars = restaurant.stars
        repeat(stars.toInt()) { index ->
            val starId =
                resources.getIdentifier("star${index + 1}", "id", requireContext().packageName)
            requireView().findViewById<View>(starId).visibility = View.VISIBLE
        }
        val hasHalfStar = stars.minus(stars.toInt()) == 0.5
        if (hasHalfStar) halfStar.visibility = View.VISIBLE

        viewModel.actualMembership?.let {
            if (it.restaurants!!.any { aRestaurant -> aRestaurant.restaurantId == restaurant.restaurantId })
                restaurantFloatingButton.setText(R.string.bookTable)
            else {
                setButtonByMembership(membershipName)
            }
        }.orElse {
            setButtonByMembership(membershipName)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            restaurantScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > oldScrollY) {
                    restaurantFloatingButton.hide()
                } else {
                    restaurantFloatingButton.show()
                }
            }
        }
    }

    private fun setButtonByMembership(membershipName: String?) {
        membershipName?.let {
            val chooseMembership = resources.getString(R.string.chooseMembership, membershipName)
            restaurantFloatingButton.text = chooseMembership
        }.orElse {
            restaurantFloatingButton.apply {
                setText(R.string.showMemberships)
                setOnClickListener {
                    findNavController().navigate(R.id.membershipsFragment)
                }
            }
        }
    }
}