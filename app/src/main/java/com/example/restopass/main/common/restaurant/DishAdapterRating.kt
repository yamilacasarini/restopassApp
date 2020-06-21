package com.example.restopass.main.common.restaurant

import android.widget.Toast
import com.example.restopass.domain.Dish
import kotlinx.android.synthetic.main.dish_item.view.*

class DishAdapterRating(
    private val dishes: List<Dish>,
    private val dpCalculation: Float
) : DishAdapter(dishes, false) {

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.apply {
            dishCardView.layoutParams.height = (250 * dpCalculation).toInt()
            dishItem.setOnClickListener {
                Toast.makeText(it.context, "Clicked on dish: ${dishes[position].name}", Toast.LENGTH_LONG).show();
            }
        }
    }

}