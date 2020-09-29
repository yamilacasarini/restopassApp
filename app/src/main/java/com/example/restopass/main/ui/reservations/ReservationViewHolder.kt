package com.example.restopass.main.ui.reservations

import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.utils.setPaddingBottom
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.Reservation
import com.example.restopass.domain.UserReservation
import com.google.android.gms.common.util.Strings
import kotlinx.android.synthetic.main.reservations_list_items.view.*
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*


class ReservationHolder(
    val inflater: LayoutInflater,
    val parentReservation: ViewGroup,
    private val reservationsFragment: ReservationsFragment
) :
    RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.reservations_list_items,
            parentReservation,
            false
        )
    ) {

    fun bind(reservation: Reservation) {
        this.applyCommons(reservation)

        itemView.apply {
            if (reservation.state == "DONE") {
                reservationAction.visibility = View.GONE
                reservationQrButton.visibility = View.GONE
                reservationStatus.setText(R.string.reservation_status_done)
                reservationStatus.setTextColor(Color.parseColor("#87000000"))
                itemView.reservationStatus.setPaddingBottom(30)
                reservationCard?.setBackgroundColor(Color.GRAY)

                reservationAction.setOnClickListener {
                    it.findNavController().navigate(
                        R.id.restaurantRatingFragment,
                        bundleOf("restaurantId" to reservation.restaurantId)
                    )
                }
            }

            val dialogClickListener =
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            reservationsFragment.cancelReservation(reservation.reservationId)
                        }
                    }
                }

            val dialogClickListenerInvitation =
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            reservationsFragment.rejectReservation(reservation.reservationId)
                        }
                    }
                }

            if (reservation.state == "CONFIRMED") {
                if (reservation.ownerUser.userId == AppPreferences.user.email) {
                    reservationAction.setText(R.string.reservation_action_cancel)
                    reservationAction.setOnClickListener {
                        val builder = androidx.appcompat.app.AlertDialog.Builder(itemView.context)
                        builder.setMessage(R.string.reservation_cancel_alert)
                            .setPositiveButton("Si", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show()
                    }
                } else {
                    reservationAction.setText(R.string.reservation_action_cancel_invitation)
                    val builder = androidx.appcompat.app.AlertDialog.Builder(itemView.context)
                    builder.setMessage(R.string.reservation_cancel_alert)
                        .setPositiveButton("Si", dialogClickListenerInvitation)
                        .setNegativeButton("No", dialogClickListenerInvitation).show()

                }
            }

            reservationQrButton?.setText(R.string.reservation_show_qr)
            reservationQrButton?.setOnClickListener {
                findNavController().navigate(
                    R.id.qrDetailFragment,
                    bundleOf("reservationId" to reservation.reservationId)
                );
            }
            reservationStatus?.setText(R.string.reservation_status_confirmed)
            reservationStatus?.setTextColor(Color.parseColor("#00b686"))
            reservationCard?.setBackgroundColor(Color.parseColor("#00b686"))


            if (reservation.state == "CANCELED") {
                reservationAction?.visibility = View.GONE
                reservationQrButton?.visibility = View.GONE
                reservationStatus?.setText(R.string.reservation_status_canceled)
                reservationStatus?.setTextColor(Color.parseColor("#d11a2a"))
                itemView.reservationStatus.setPaddingBottom(30)
                reservationCard?.setBackgroundColor(Color.parseColor("#d11a2a"))
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateToHuman(date: String, diners: Int): String? {
        val dt = LocalDateTime.parse(date)

        val dayName = dt.dayOfWeek.getDisplayName(
            TextStyle.FULL,
            Locale("es")
        )

        val monthName = dt.month.getDisplayName(TextStyle.FULL, Locale("es"))

        val hour: String
        hour = if (dt.minute == 0) {
            dt.hour.toString() + ":00"
        } else {
            dt.hour.toString() + ":" + dt.minute
        }

        return dayName.capitalize() + " " + dt.dayOfMonth + " de " + monthName.capitalize() + " de " + dt.year + " a las " + hour + "hs " + diners + "pers"
    }

    private fun transformName(user: UserReservation): String {
        return user.name + " " + user.lastName
    }

    fun applyCommons(reservation: Reservation) {
        itemView.apply {
            Glide.with(itemView.context).load(reservation.img).into(reservationImage!!);
            reservationTitle.text = reservation.restaurantName
            reservationAddress.text = reservation.restaurantAddress

            if (reservation.invitation) {
                val msg = itemView.context.getString(
                    R.string.reservation_invitation,
                    transformName(reservation.ownerUser)
                )
                reservationInvitation.text = msg
                reservationInvitation.visibility = View.VISIBLE
            } else {
                reservationInvitation.visibility = View.GONE
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                reservationDate?.text = dateToHuman(reservation.date, reservation.dinners)
            } else {
                reservationDate?.text = reservation.date
            }

            reservationArrow?.setOnClickListener {
                if (reservationDinersModule?.visibility!! == View.GONE) {
                    reservationDinersModule?.visibility = View.VISIBLE
                    Glide.with(itemView.context).load(R.drawable.ic_arrow_up_24dp).fitCenter()
                        .into(reservationArrow!!)
                } else {
                    reservationDinersModule?.visibility = View.GONE
                    Glide.with(itemView.context).load(R.drawable.ic_arrow_down_24dp).fitCenter()
                        .into(reservationArrow!!)
                }
            }

            var confirmedUsersString =
                reservation.confirmedUsers?.joinToString("\n") { it.name + " " + it.lastName }
            var toConfirmUsersString =
                reservation.toConfirmUsers?.joinToString("\n") { it.name + " " + it.lastName }


            if (Strings.isEmptyOrWhitespace(confirmedUsersString) && Strings.isEmptyOrWhitespace(
                    toConfirmUsersString
                )
            ) {
                reservationArrow.visibility = View.GONE
            } else {
                reservationArrow.visibility = View.VISIBLE
                if (!Strings.isEmptyOrWhitespace(confirmedUsersString)) {
                    reservationDinersConfirmTitle?.setText(R.string.reservation_diners_confirmed)
                    reservationDinersConfirm.text = confirmedUsersString
                } else {
                    reservationDinersConfirmTitle.visibility = View.GONE
                    reservationDinersConfirm.visibility = View.GONE
                }

                if (!Strings.isEmptyOrWhitespace(toConfirmUsersString)) {
                    reservationDinersToConfirmTile?.setText(R.string.reservation_diners_toConfirm)
                    reservationDinersToConfirm.text = toConfirmUsersString
                    reservationDinersToConfirmTile.visibility = View.VISIBLE
                    reservationDinersToConfirm.visibility = View.VISIBLE
                } else {
                    reservationDinersToConfirmTile.visibility = View.GONE
                    reservationDinersToConfirm.visibility = View.GONE
                }
            }
        }
    }

    fun bindPending(reservation: Reservation) {
        applyCommons(reservation)

        itemView.apply {
            reservationAction?.setText(R.string.pending_reservation_cancel)
            reservationAction?.setOnClickListener {
                reservationsFragment.rejectReservation(reservation.reservationId)
            }
            reservationQrButton?.setText(R.string.pending_reservation_accept)
            reservationQrButton?.setOnClickListener {
                reservationsFragment.confirmReservation(reservation.reservationId)
            }
            reservationStatus.setText(R.string.reservation_status_pending)
            reservationStatus?.setTextColor(Color.parseColor("#EF7215"))
            reservationCard?.setBackgroundColor(Color.parseColor("#EF7215"))
        }

    }


}