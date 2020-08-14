package com.example.restopass.restaurantApp.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restopass.R
import com.example.restopass.domain.Dish
import com.example.restopass.domain.DoneReservationViewModel
import kotlinx.android.synthetic.main.restaurant_fragment_list_dishes.*
import kotlinx.android.synthetic.main.restaurant_fragment_list_dishes.view.*
import java.time.LocalDateTime

class RestaurantDishesFragment : Fragment() {

    private lateinit var doneReservationViewModel: DoneReservationViewModel
    private lateinit var restaurantDishesAdapter: RestaurantDishesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.restaurant_fragment_list_dishes, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doneReservationViewModel =
            ViewModelProvider(requireActivity()).get(DoneReservationViewModel::class.java)

        restaurantDishesAdapter = RestaurantDishesAdapter()
        val linearLayoutManager = LinearLayoutManager(activity)
        dishesRecycler.apply {
            layoutManager = linearLayoutManager
            adapter = restaurantDishesAdapter
        }

        activity?.window?.statusBarColor = resources.getColor(R.color.restoPassGreen)

        view.apply {
            dishesTitle.text = resources.getString(
                R.string.dishesReservationNumber,
                doneReservationViewModel.doneReservation.reservationId
            )
            dishesOwnerText.text = formatName()
            dishesGuestsText.text = formatGuests()
            dishesDateText.text = formatDate()

            restaurantDishesAdapter.list = formatDishes().toList()
            restaurantDishesAdapter.notifyDataSetChanged()
        }
    }

    private fun formatName(): String {
        val name = doneReservationViewModel.doneReservation.ownerUserName.split(" ")
        return "${name[0]}\n${name[1]}"
    }

    private fun formatGuests(): String {
        return "${doneReservationViewModel.doneReservation.dinners}\npers"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(): String {
        val date = LocalDateTime.parse(doneReservationViewModel.doneReservation.date)
        return if (date.minute == 0) {
            "${date.dayOfMonth}/${date.monthValue}\n${date.hour}:0${date.minute}"
        } else {
            "${date.dayOfMonth}/${date.monthValue}\n${date.hour}:${date.minute}"
        }
    }

    private fun formatDishes(): Map<String, List<Dish>> {
        return doneReservationViewModel.doneReservation.dishesPerMembership.mapKeys {
            if (doneReservationViewModel.doneReservation.dinnersPerMembership[it.key] == 0L) {
                resources.getString(R.string.dishesRowTitleWoDinners, it.key)
            } else {
                resources.getString(
                    R.string.dishesRowTitle,
                    it.key,
                    doneReservationViewModel.doneReservation.dinnersPerMembership[it.key].toString()
                )
            }
        }
    }
}