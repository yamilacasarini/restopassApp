package com.example.restopass.main.common.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.style
import com.example.restopass.R
import kotlinx.android.synthetic.main.view_tag_item.view.*

class TagAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tags: List<String> = listOf()

    class TagViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = tags.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_tag_item, parent, false)

        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            restaurantTag.text =  tags[position]

            if (position == tags.size - 1) {
                restaurantTag.style(R.style.tagLastListItem)
            }
        }
    }


}