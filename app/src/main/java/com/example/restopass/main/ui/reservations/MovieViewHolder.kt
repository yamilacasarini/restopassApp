package com.example.restopass.main.ui.reservations

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R

class MovieViewHolder(inflater: LayoutInflater
                      , parent: ViewGroup
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_items, parent, false)) {
    private var mImageView: ImageView? = null
    private var mTitleView: TextView? = null
    private var mAddressView: TextView? = null
    private var mDateView: TextView? = null
    private var mStatusView: TextView? = null


    init {
        mImageView = itemView.findViewById(R.id.reservation_image)
        mTitleView = itemView.findViewById(R.id.reservation_title)
        mAddressView = itemView.findViewById(R.id.reservation_address)
        mDateView = itemView.findViewById(R.id.reservation_date)
        mStatusView = itemView.findViewById(R.id.reservation_status)
    }

    fun bind(reservation: ReservationsFragment.ReservationData) {
        mImageView?.setImageResource(R.drawable.nicolas_cage)
        mTitleView?.text = reservation.title
        mAddressView?.text = reservation.address
        mDateView?.text = reservation.date
        mStatusView?.text = reservation.status
    }
}