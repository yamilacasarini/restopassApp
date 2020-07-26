package com.example.restopass.main.ui.reservations

import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.domain.Reservation
import com.example.restopass.domain.ReservationViewModel
import com.example.restopass.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_qr_detail.view.*

class QrDetailFragment : Fragment() {

    private lateinit var reservationsViewModel: ReservationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reservationsViewModel =
            ViewModelProvider(requireActivity()).get(ReservationViewModel::class.java)
        val reservation : Reservation? = reservationsViewModel.reservations.find { it.reservationId == arguments?.getString("reservationId") }

        view.apply {
            Glide.with(context).load(decodeQr(reservation?.qrBase64))
                .into(qrImage)

            qrReservationId.text = Html.fromHtml(resources.getString(R.string.qr_detail_error_reservation, reservation?.reservationId))
            qrUserId.text = Html.fromHtml(resources.getString(R.string.qr_detail_error_user,reservation?.ownerUser?.userId))
        }



        (activity as MainActivity).topAppBar?.apply {
            title = resources.getString(R.string.qr_detail, reservation?.restaurantName)
            visibility = View.VISIBLE
        }

    }

    private fun decodeQr(qrBase64: String?): ByteArray? {
        val pureBase64Encoded = qrBase64?.substring(qrBase64.indexOf(",") + 1)
        return Base64.decode(pureBase64Encoded, Base64.DEFAULT)
    }
}