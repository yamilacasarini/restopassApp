package com.example.restopass.main.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter(private val list: List<HomeFragment.PlanData>, val listener: HomeListener): RecyclerView.Adapter<HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val plan: HomeFragment.PlanData = list[position]
        holder.bind(plan)
    }

    override fun getItemCount(): Int = list.size
}

class HomeListener(val clickListener: (plan: HomeFragment.PlanData) -> Unit) {
    fun onClick(plan: HomeFragment.PlanData) = clickListener(plan)
}
