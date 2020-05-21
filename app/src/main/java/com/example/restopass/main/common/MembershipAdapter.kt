package com.example.restopass.main.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.databinding.ViewMembershipItemBinding
import kotlinx.android.synthetic.main.view_membership_item.view.*

class MembershipAdapter(val listener: MembershipListener) :
    RecyclerView.Adapter<MembershipAdapter.MembershipViewHolder>() {

    var memberships: List<Membership> = listOf()

    override fun getItemCount() = memberships.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembershipViewHolder = MembershipViewHolder.from(parent)

    override fun onBindViewHolder(holder: MembershipViewHolder, position: Int) {
        holder.itemView.apply {
            this.id = position
            Glide.with(this).load(memberships[position].image).into(image)
            restaurantsAmount.text = memberships[position].restaurants.size.toString()

            val dishes = memberships[position].restaurants.flatMap { it.dishes }.size
            dishesAmount.text = dishes.toString()
        }

        holder.binding.membership = memberships[position]

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