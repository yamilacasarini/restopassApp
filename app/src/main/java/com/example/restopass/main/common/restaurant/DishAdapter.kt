package com.example.restopass.main.common.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.domain.Dish
import kotlinx.android.synthetic.main.dish_item.view.*

class DishAdapter(private val dishes: List<Dish>) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {
    var membershipName: Int? = null

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

            val stars = dish.stars
            repeat(stars.toInt()) { index ->
                val starId =
                    resources.getIdentifier("star${index + 1}", "id", context.packageName)
                findViewById<View>(starId).visibility = View.VISIBLE
            }
            val hasHalfStar = stars.minus(stars.toInt()) == 0.5
            if (hasHalfStar) halfStar.visibility = View.VISIBLE

            membershipName?.let {

            }
        }
    }
}