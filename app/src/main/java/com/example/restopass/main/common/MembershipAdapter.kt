package com.example.restopass.main.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.databinding.ViewMembershipItemBinding
import kotlinx.android.synthetic.main.view_membership_item.view.*

class MembershipAdapter(private val membership: List<Membership>, val listener: MembershipListener) :
    RecyclerView.Adapter<MembershipAdapter.MembershipViewHolder>() {

    override fun getItemCount() = membership.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembershipViewHolder = MembershipViewHolder.from(parent)

    override fun onBindViewHolder(holder: MembershipViewHolder, position: Int) {
        holder.itemView.apply {
            this.id = position
            Glide.with(this).load(membership[position].image).into(image)

            restaurantButton.setOnClickListener {
               if (restaurantsList.visibility == View.GONE)  {
                   restaurantsList.visibility = View.VISIBLE
                   restaurantButton.setImageResource(R.drawable.ic_arrow_up_24dp)
               } else {
                   restaurantsList.visibility = View.GONE
                   restaurantButton.setImageResource(R.drawable.ic_arrow_down_24dp)
               }
            }
        }

        holder.binding.membership = membership[position]

    }

    class MembershipViewHolder private constructor(val binding: ViewMembershipItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): MembershipViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ViewMembershipItemBinding.inflate(layoutInflater, parent, false)

                return MembershipViewHolder(binding)
            }
        }
    }
}

interface MembershipListener {
    fun onClick(membership: Membership)
}