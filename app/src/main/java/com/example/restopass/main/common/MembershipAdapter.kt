package com.example.restopass.main.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import kotlinx.android.synthetic.main.view_membership_item.view.*

class MembershipAdapter() :
    RecyclerView.Adapter<MembershipAdapter.MembershipViewHolder>() {

    var memberships: List<Membership> = listOf()

    class MembershipViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = memberships.size

    override fun getItemViewType(position: Int): Int {
        return if (memberships[position].isTitle)
            VIEWTYPE_TITLE
        else
            VIEWTYPE_MEMBERSHIP
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembershipViewHolder {
        val layout =
            if (viewType == VIEWTYPE_TITLE) R.layout.view_membership_title else R.layout.view_membership_item

        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return MembershipViewHolder(view)

    }

    override fun onBindViewHolder(holder: MembershipViewHolder, position: Int) {
        val membership = memberships[position]
        holder.itemView.apply {
            membershipTitle.text = membership.name

            if (!membership.isTitle) {
                priceTag.text = resources.getString(R.string.price_tag, membership.price.toString())

                Glide.with(this).load(membership.img).into(image)
                description.text = membership.description

                restaurantsAmount.text = membership.restaurants?.size.toString()

                val dishes = membership.restaurants?.flatMap { it.dishes }?.size
                dishesAmount.text = dishes.toString()

                if (membership.isActual) actualMembershipTextView.visibility = View.VISIBLE
                else membershipButton.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val VIEWTYPE_TITLE = 1
        private const val VIEWTYPE_MEMBERSHIP = 2
    }
}