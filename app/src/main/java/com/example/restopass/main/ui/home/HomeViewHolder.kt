package com.example.restopass.main.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R

class HomeViewHolder(inflater: LayoutInflater
                     , parent: ViewGroup
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.home_items, parent, false)) {
    private var title: TextView? = null
    private var price: TextView? = null


    init {
        title = itemView.findViewById(R.id.plan_title)
        price = itemView.findViewById(R.id.plan_price)
    }

    fun bind(plan: HomeFragment.PlanData) {
        title?.text = plan.title
        price?.text = plan.price.toPlainString()
    }
}