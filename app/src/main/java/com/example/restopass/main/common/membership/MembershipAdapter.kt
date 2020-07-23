package com.example.restopass.main.common.membership

import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.style
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.Membership
import com.example.restopass.main.ui.home.notEnrolledHome.NotEnrolledHomeFragment
import com.example.restopass.utils.AlertDialogUtils
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.view_membership_item.view.*

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
        return when (viewType) {
            VIEWTYPE_TITLE -> TitleViewHolder.from(parent)
            VIEWTYPE_MEMBERSHIP -> MembershipViewHolder.from(parent, memberships)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val membership = memberships[position]

        if (membership.isTitle) {
            holder as TitleViewHolder
            holder.bind(membership)
        } else {
            holder as MembershipViewHolder
            holder.bind(membership, parentFragment, position)
        }
    }

    class MembershipViewHolder(val view: View, val memberships: List<Membership>) :
        RecyclerView.ViewHolder(view) {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(membership: Membership, parentFragment: MembershipAdapterListener, position: Int) {
            view.apply {

                Glide.with(this).load(membership.img).into(image)

                membershipTitle.text = membership.name
                priceTag.text = resources.getString(R.string.price_tag, membership.price.toString())

                AppPreferences.user.b2BUserEmployee?.percentageDiscountPerMembership?.get(membership.membershipId!!)
                    ?.let {
                        when {
                            it == 100F -> {
                                grayPrice()
                                fillPriceDiscountInfo(
                                    resources.getString(R.string.price_free),
                                    AppPreferences.user.b2BUserEmployee!!.companyName,
                                    it.toInt().toString()
                                )
                            }
                            it != 0F -> {
                                grayPrice()
                                fillPriceDiscountInfo(
                                    resources.getString(
                                        R.string.price_tag,
                                        (membership.price!!.toFloat() * (100F - it) / 100F).toInt()
                                            .toString()
                                    ),
                                    AppPreferences.user.b2BUserEmployee!!.companyName,
                                    it.toInt().toString()
                                )
                            }
                            else -> {
                                priceDiscount.visibility = View.GONE
                                priceInfo.visibility = View.GONE
                            }
                        }
                    }

                description.text = membership.description

                restaurantsAmount.text = membership.restaurants?.size.toString()

                val dishes = membership.dishesAmount()
                dishesAmount.text = dishes.toString()

                visitsAmount.text = membership.visits.toString()

                if (membership.isActual) {
                    membershipButton.setText(R.string.actual_membership)
                    membershipButton.setBackgroundColor(resources.getColor(R.color.cancel))
                    membershipButton.setOnClickListener {
                        parentFragment.onCancelMembershipClick()
                    }
                } else {
                    membershipButton.setOnClickListener {
                        parentFragment.onEnrollClick(membership)
                    }
                }
                detailsButton.setOnClickListener {
                    parentFragment.onDetailsClick(membership)
                    findNavController().navigate(R.id.restaurantsListFragment)
                }


                if (parentFragment is MembershipFragment) {
                    membershipCard.style(R.style.membershipCard)
                }

                if (parentFragment is NotEnrolledHomeFragment) {
                    if (position == memberships.size - 1) membershipCard.style(R.style.membershipVerticalLastCard)
                    else membershipCard.style(R.style.membershipVerticalCard)
                }
            }
        }

        private fun grayPrice() {
            view.apply {
                priceTag.style(R.style.priceTagDark)
                priceTag.paintFlags = priceTag.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

        private fun fillPriceDiscountInfo(
            msgDiscount: String,
            discount: String,
            companyName: String
        ) {
            view.apply {
                priceInfo.setOnClickListener {
                    AlertDialogUtils.buildAlertDialog(
                        null,
                        LayoutInflater.from(context),
                        container,
                        msg = resources.getString(
                            R.string.price_alert, discount, companyName
                        )
                    ).show()
                }
                priceDiscount.text = msgDiscount
                priceDiscount.visibility = View.VISIBLE
                priceInfo.visibility = View.VISIBLE
            }
        }

        companion object {
            fun from(parent: ViewGroup, memberships: List<Membership>): MembershipViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.view_membership_item, parent, false)
                return MembershipViewHolder(view, memberships)
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
    fun onDetailsClick(membership: Membership)
    fun onEnrollClick(membership: Membership)
    fun onCancelMembershipClick()
}

