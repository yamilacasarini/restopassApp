package com.example.restopass.main.common.restaurant

import com.example.restopass.domain.Dish
import kotlinx.android.synthetic.main.dish_item.view.*

class DishAdapterRating(
    override var dishes: List<Dish>,
    private val dpCalculation: Float,
    private val ratingFragment: RestaurantRatingFragment
) : DishAdapter(dishes, false) {

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.apply {
            dishItem.setOnClickListener {
                ratingFragment.onDishSelected(dishes[position])
            }
        }
    }

}