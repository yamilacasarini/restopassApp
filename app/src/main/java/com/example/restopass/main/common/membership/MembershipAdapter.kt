package com.example.restopass.main.common.membership

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.domain.Membership
import kotlinx.android.synthetic.main.view_membership_item.view.*
import java.lang.ClassCastException

class MembershipAdapter(private val parentFragment: MembershipAdapterListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var memberships: List<Membership> = listOf()

    override fun getItemCount() = memberships.size

    override fun getItemViewType(position: Int): Int {
        return if (memberships[position].isTitle)
            VIEWTYPE_TITLE
        else
            VIEWTYPE_MEMBERSHIP
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEWTYPE_TITLE -> TitleViewHolder.from(parent)
            VIEWTYPE_MEMBERSHIP -> MembershipViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val membership = memberships[position]

        if (membership.isTitle)  {
            holder as TitleViewHolder
            holder.bind(membership)
        } else {
            holder as MembershipViewHolder
            holder.bind(membership, parentFragment)
        }
    }

    class MembershipViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(membership: Membership, parentFragment: MembershipAdapterListener) {
            view.apply {
                membershipTitle.text = membership.name
                priceTag.text = resources.getString(R.string.price_tag, membership.price.toString())

                Glide.with(this).load(membership.img).into(image)

                if (parentFragment is MembershipFragment) {
                    membershipCard.layoutParams.apply {
                        this.width = ViewGroup.LayoutParams.MATCH_PARENT
                        membershipCard.layoutParams = this
                    }

                    image.layoutParams.apply {
                        this.height = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            194.toFloat(),
                            resources.displayMetrics
                        ).toInt()
                        image.layoutParams = this
                    }
                }

                description.text = membership.description

                restaurantsAmount.text = membership.restaurants?.size.toString()

                val dishes = membership.restaurants?.flatMap { it.dishes }?.size
                dishesAmount.text = dishes.toString()

                visitsAmount.text = membership.visits.toString()

                if (membership.isActual) {
                    membershipButton.setText(R.string.actual_membership)
                    membershipButton.isEnabled = false
                } else {
                    membershipButton.setOnClickListener {

                    }
                }
                detailsButton.setOnClickListener {
                    parentFragment.onClick(membership)
                    findNavController().navigate(R.id.restaurantsListFragment)
                }
            }
        }
        companion object {
            fun from(parent: ViewGroup): MembershipViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.view_membership_item, parent, false)
                return MembershipViewHolder(view)
            }
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(membership: Membership) {
            view.membershipTitle.text = membership.name
        }

        companion object {
            fun from(parent: ViewGroup): TitleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.view_membership_title, parent, false)
                return TitleViewHolder(view)
            }
        }
    }

    companion object {
        private const val VIEWTYPE_TITLE = 1
        private const val VIEWTYPE_MEMBERSHIP = 2
    }
}

interface MembershipAdapterListener {
    fun onClick(membership: Membership)
}

