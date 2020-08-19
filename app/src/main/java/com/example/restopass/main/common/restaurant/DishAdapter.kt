package com.example.restopass.main.common.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.style
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.common.orElse
import com.example.restopass.domain.Dish
import com.example.restopass.domain.Membership
import kotlinx.android.synthetic.main.dish_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class DishAdapter(
    open var dishes: List<Dish> = listOf(),
    private val showStars: Boolean = true,
    private val listener: DishAdapterListener? = null,
    private val showAvailability: Boolean = true
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {
    var selectedMembership: Membership? = null

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    class DishViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = dishes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dish_item, parent, false)
        return DishViewHolder(view)
    }


    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]

        holder.itemView.apply {
            Glide.with(this).load(dish.img).into(dishImage)
            dishName.text = dish.name
            dishDescription.text = dish.description

            if (showStars) {
                dishStars.rating = dish.stars
            } else {
                dishStars.visibility = View.GONE
            }

            // Si viene de una tarjeta Membresía, separamos los platos disponibles con los que no según dicha membresía
            // Sino, vemos si tiene una membresía actual y hacemos lo mismo pero en base a la meembresía actual
            // Si tampoco tiene membresía actual, mostramos todos como no disponibles y la leyenda "Desde X"
            //Si viene desde la app de usuarios restaurant con showAvailability false no mostramos la leyenda de Desde
            //Si viene desde la app de usuarios restaurant con showAvailability true mostramos todos como no disponibles y la leyenda "Desde X"
            if (showAvailability) {
                if (AppPreferences.restaurantUser != null) {
                    setNotAvailable(holder, dish.baseMembershipName)
                } else {
                    selectedMembership?.let {
                        setAvailability(holder, dish, it.membershipId!!)
                    }.orElse {
                        AppPreferences.user.actualMembership?.let {
                            setAvailability(holder, dish, it)
                        }.orElse {
                            setNotAvailable(holder, dish.baseMembershipName)
                        }
                    }
                }
            }

            if (position == dishes.size - 1) {
                dishCard.style(R.style.dishVerticalLastCard)
            }

            if (listener != null) {
                this.setOnClickListener {
                    coroutineScope.launch {
                        listener.onDishClick(dish.restaurantId!!)
                    }
                }
            }
        }
    }

    private fun setNotAvailable(holder: DishViewHolder, baseMembershipName: String) {
        holder.itemView.apply {
            notAvailableDishText.text =
                resources.getString(
                    R.string.notAvailableDish,
                    baseMembershipName
                )
            notAvailableDishText.visibility = View.VISIBLE
        }
    }

    private fun setAvailability(holder: DishViewHolder, dish: Dish, membershipId: Int) {
        holder.itemView.apply {
            if (!dish.isIncluded(membershipId)) {
                notAvailableDishText.text =
                    resources.getString(R.string.notAvailableDish, dish.baseMembershipName)
                notAvailableDishText.visibility = View.VISIBLE
            }
        }
    }
}

interface DishAdapterListener {
    suspend fun onDishClick(restaurantId: String)
}