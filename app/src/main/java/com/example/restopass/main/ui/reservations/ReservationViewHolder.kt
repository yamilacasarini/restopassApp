package com.example.restopass.main.ui.reservations

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R

class MovieViewHolder(inflater: LayoutInflater
                      , parent: ViewGroup
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.reservations_list_items, parent, false)) {
    private var mCardView : CardView? = null;
    private var mImageView: ImageView? = null
    private var mTitleView: TextView? = null
    private var mAddressView: TextView? = null
    private var mDateView: TextView? = null
    private var mStatusView: TextView? = null
    private var mActionView: TextView? = null

    init {
        mCardView = itemView.findViewById(R.id.reservation_card)
        mImageView = itemView.findViewById(R.id.reservation_image)
        mTitleView = itemView.findViewById(R.id.reservation_title)
        mAddressView = itemView.findViewById(R.id.reservation_address)
        mDateView = itemView.findViewById(R.id.reservation_date)
        mStatusView = itemView.findViewById(R.id.reservation_status)
        mActionView = itemView.findViewById(R.id.reservation_action)
    }

    fun bind(reservation: ReservationsFragment.ReservationData) {
        mImageView?.setImageResource(R.drawable.pastas)
        mTitleView?.text = reservation.title
        mAddressView?.text = reservation.address
        mDateView?.text = reservation.date
        if(reservation.status == "DONE") {
            mActionView?.setText(R.string.reservation_action_review)
            mStatusView?.setText(R.string.reservation_status_done)
            mCardView?.setBackgroundColor(Color.GRAY)
        }

        if(reservation.status == "CONFIRMED") {
            mActionView?.setText(R.string.reservation_action_cancel)
            mStatusView?.setText(R.string.reservation_status_confirmed)
            mStatusView?.setTextColor(Color.parseColor("#00b686"))
            mCardView?.setBackgroundColor(Color.parseColor("#00b686"))
        }

        if(reservation.status == "CANCELED") {
            mActionView?.setText(R.string.reservation_action_canceled)
            mStatusView?.setText(R.string.reservation_status_canceled)
            mStatusView?.setTextColor(Color.parseColor("#d11a2a"))
            mCardView?.setBackgroundColor(Color.parseColor("#d11a2a"))
        }
    }
}