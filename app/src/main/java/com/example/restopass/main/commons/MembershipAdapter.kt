package com.example.restopass.main.commons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.databinding.ViewMembershipItemBinding
import kotlinx.android.synthetic.main.view_membership_item.view.*

class MembershipAdapter(private val membership: List<Membership>, val listener: MembershipListener) :
    ListAdapter<Membership, RecyclerView.ViewHolder>(MembershipDiffCallback()) {

    //EL ORDEN DE LLAMADAS ES ESTE
    override fun getItemCount() = membership.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MembershipViewHolder.from(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            this.id = position
            membershipTitle.text = membership[position].title
            Glide.with(this).load(membership[position].image).into(membershipImage)
            membershipDescription.text = membership[position].description
            restaurantsList.visibility = membership[position].restaurantsVisibility
        }

        if (holder is MembershipViewHolder) holder.bind(listener, membership[position])
    }

    class MembershipViewHolder private constructor(val binding: ViewMembershipItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: MembershipListener, item: Membership) {
            binding.membership = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MembershipViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ViewMembershipItemBinding.inflate(layoutInflater, parent, false)

                return MembershipViewHolder(binding)
            }
        }
    }
}

class MembershipDiffCallback : DiffUtil.ItemCallback<Membership>() {
    override fun areItemsTheSame(old: Membership, aNew: Membership) = old.type == aNew.type
    override fun areContentsTheSame(old: Membership, aNew: Membership): Boolean = true
}

class MembershipListener(val clickListener: (membership: Membership?) -> Unit) {
    fun onClick(membership: Membership) = clickListener(membership)
}