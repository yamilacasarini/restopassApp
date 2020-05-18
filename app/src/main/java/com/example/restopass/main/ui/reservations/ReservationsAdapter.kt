package com.example.restopass.main.ui.reservations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ReservationsAdapter(private val list: List<ReservationsFragment.ReservationData>)
    : RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val restaurant: ReservationsFragment.ReservationData = list[position]
        holder.bind(restaurant)
    }

    override fun getItemCount(): Int = list.size

}