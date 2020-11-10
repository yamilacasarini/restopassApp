package com.example.restopass.restaurantApp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.domain.Dish
import com.example.restopass.main.common.restaurant.DishAdapter
import kotlinx.android.synthetic.main.restaurant_fragment_dishes_list_item.view.*

class RestaurantDishesAdapter(val parent : Fragment) : RecyclerView.Adapter<DishesHolder>() {

    var list: List<Pair<String, List<Dish>>> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishesHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DishesHolder(inflater, parent, this.parent)
    }

    override fun onBindViewHolder(holder: DishesHolder, position: Int) {
        val dishesRow: Pair<String, List<Dish>> = list[position]
        holder.bind(dishesRow)
    }

    override fun getItemCount(): Int = list.size

}

class DishesHolder(
    val inflater: LayoutInflater,
    parentCreateReservation: ViewGroup,
    val parent: Fragment
) :
    RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.restaurant_fragment_dishes_list_item,
            parentCreateReservation,
            false
        )
    ) {

    lateinit var dishAdapter : DishAdapter

    fun bind(dishesRow: Pair<String, List<Dish>>) {

        dishAdapter =  DishAdapter(dishesRow.second, parent = parent,  container = itemView.dishContainer)

        itemView.apply {
            dishesRowTitle.text = dishesRow.first
            dishesItemsRecycler.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = dishAdapter
            }
        }
    }

}

