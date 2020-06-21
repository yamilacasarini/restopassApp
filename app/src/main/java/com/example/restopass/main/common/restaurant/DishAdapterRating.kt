package com.example.restopass.main.common.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.common.orElse
import com.example.restopass.domain.Dish
import com.example.restopass.domain.Membership
import kotlinx.android.synthetic.main.dish_item.view.*

class DishAdapterRating(private val dishes: List<Dish>) : DishAdapter(dishes) {

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.apply {
            dishItem.setOnClickListener {
                Toast.makeText(it.context, "Clicked on dish: ${dishes[position].name}", Toast.LENGTH_LONG).show();
            }
        }
    }

}