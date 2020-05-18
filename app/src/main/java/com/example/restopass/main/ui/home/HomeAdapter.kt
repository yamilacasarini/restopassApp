package com.example.restopass.main.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R

class HomeAdapter(private val list: List<PlanData>): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var listener: HomeListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val plan: PlanData = list[position]
        holder.bind(plan)
        holder.itemView.setOnClickListener { listener?.onClick(list[position]) }
    }

    override fun getItemCount(): Int = list.size

    inner class HomeViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(inflater.inflate(R.layout.home_items, parent, false)) {
        private var title: TextView? = null
        private var price: TextView? = null

        init {
            title = itemView.findViewById(R.id.plan_title)
            price = itemView.findViewById(R.id.plan_price)
        }

        fun bind(plan: PlanData) {
            title?.text = plan.title
            price?.text = plan.price.toString()
        }

    }
}

interface HomeListener {
    fun onClick(plan: PlanData?)
}

