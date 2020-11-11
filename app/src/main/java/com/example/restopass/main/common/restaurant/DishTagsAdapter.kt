package com.example.restopass.main.common.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.modal_dish_tag_item.view.*

class DishTagsAdapter(
    private var dishesImg: List<String> = listOf()
) : RecyclerView.Adapter<DishTagsAdapter.DishTagHolder>() {

    class DishTagHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = dishesImg.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishTagHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.modal_dish_tag_item, parent, false)
        return DishTagHolder(view)
    }

    override fun onBindViewHolder(holder: DishTagHolder, position: Int) {
        val dishImg = dishesImg[position]

        holder.itemView.apply {
            val id = context.resources
                .getIdentifier(dishImg.toLowerCase() + "_icon", "drawable", context.packageName)
            Glide.with(this).load(id).into(dishTagImg)
        }
    }

}