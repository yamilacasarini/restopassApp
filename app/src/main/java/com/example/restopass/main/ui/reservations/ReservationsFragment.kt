package com.example.restopass.main.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.Reservation
import com.example.restopass.domain.ReservationViewModel
import com.example.restopass.main.common.AlertBody
import com.example.restopass.utils.AlertDialogUtils
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reservations.*
import kotlinx.coroutines.*
import timber.log.Timber


class ReservationsFragment : Fragment() {

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var reservationsAdapter: ReservationsAdapter

    private lateinit var pendingReservationAdapter: ReservationsAdapter

    private lateinit var reservationsViewModel: ReservationViewModel

    private lateinit var rootView: View;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        reservationsViewModel =
            ViewModelProvider(requireActivity()).get(ReservationViewModel::class.java)

        rootView = inflater.inflate(R.layout.fragment_reservations, container, false)
        return rootView;
    }

    override fun onStart() {
        super.onStart()

        pendingReservations.setOnClickListener {
            if (pendingReservationsRecyclerView.visibility == View.GONE) {
                hideReservations()
                showPendingReservations()
            } else {
                hidePendingReservations()
            }
        }

        reservations.setOnClickListener {
            if (reservationsArrow.visibility == View.GONE) {
                return@setOnClickListener
            }
            if (reservationsRecyclerView.visibility == View.GONE) {
                hidePendingReservations()
                showReservations()
            } else {
                hideReservations()
            }
        }

        reservationLoader.visibility = View.VISIBLE
        reservationListsContainer.visibility = View.GONE
        coroutineScope.launch {
            try {
                reservationsViewModel.get()

                if (reservationsViewModel.reservations.isEmpty()) {
                    findNavController().navigate(R.id.emptyReservationFragment)
                }

                notifyReservations();
                notifyPendingReservations();

                reservationLoader.visibility = View.GONE
                reservationListsContainer.visibility = View.VISIBLE

                arguments?.get("reservationId")?.apply {
                    val reservationIndex = reservationsViewModel.reservations
                        .indexOfFirst { it.reservationId == this }

                    if (reservationIndex >= 0) {
                        if(reservationsViewModel.reservations.get(reservationIndex).invitation){
                            showPendingReservations()
                            pendingReservationsRecyclerView.doOnLayout {
                                reservationsRecyclerView.smoothScrollToPosition(reservationIndex)
                            }
                        } else {
                            reservationsRecyclerView.doOnLayout {
                                reservationsRecyclerView.smoothScrollToPosition(reservationIndex)
                            }
                        }
                    }

                }

            } catch (e: ApiException) {
                if (isActive) {
                    Timber.e(e)
                    reservationLoader.visibility = View.GONE
                    reservationListsContainer.visibility = View.VISIBLE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        reservationsAdapter = ReservationsAdapter(this)
        pendingReservationAdapter = ReservationsAdapter(this)
        val linearLayoutManager = LinearLayoutManager(activity)
        val pendingLinearLayourManager = LinearLayoutManager(activity)
        reservationsRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = reservationsAdapter
        }

        pendingReservationsRecyclerView.apply {
            layoutManager = pendingLinearLayourManager
            adapter = pendingReservationAdapter
        }
    }

    fun cancelReservation(reservationId: String) {
        reservationLoader.visibility = View.VISIBLE
        reservationListsContainer.visibility = View.GONE
        coroutineScope.launch {
            try {
                reservationsViewModel.cancel(reservationId)
                reservationsAdapter.list = reservationsViewModel.reservations

                if (reservationsViewModel.reservations.isEmpty()) {
                    findNavController().navigate(R.id.emptyReservationFragment)
                } else {
                    reservationsAdapter.notifyDataSetChanged()
                }

                reservationLoader.visibility = View.GONE
                reservationListsContainer.visibility = View.VISIBLE
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    reservationLoader.visibility = View.GONE
                    reservationListsContainer.visibility = View.VISIBLE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                }
            }
        }

    }

    fun confirmReservation(reservationId: String) {
        reservationListsContainer.visibility = View.GONE
        reservationLoader.visibility = View.VISIBLE
        coroutineScope.launch {
            try {
                reservationsViewModel.confirm(reservationId)
                reservationLoader.visibility = View.GONE
                reservationListsContainer.visibility = View.VISIBLE

                if (reservationsViewModel.reservations.isEmpty()) {
                    findNavController().navigate(R.id.emptyReservationFragment)
                } else {
                    notifyReservations()
                    notifyPendingReservations()
                    AlertDialogUtils.buildAlertDialog(null, layoutInflater, container, view,
                        AlertBody(description = getString(R.string.reservationConfirmed))).show()
                }

            } catch (e: com.example.restopass.connection.Api4xxException) {
                if (isActive) {
                    Timber.e(e)
                    reservationLoader.visibility = View.GONE
                    reservationListsContainer.visibility = View.VISIBLE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                }
            }
        }

    }

    fun rejectReservation(reservationId: String) {
        reservationLoader.visibility = View.VISIBLE
        reservationListsContainer.visibility = View.GONE
        coroutineScope.launch {
            try {
                reservationsViewModel.reject(reservationId)
                reservationLoader.visibility = View.GONE
                reservationListsContainer.visibility = View.VISIBLE

                if (reservationsViewModel.reservations.isEmpty()) {
                    findNavController().navigate(R.id.emptyReservationFragment)
                } else {
                    notifyReservations()
                    notifyPendingReservations()
                }

            } catch (e: com.example.restopass.connection.Api4xxException) {
                if (isActive) {
                    Timber.e(e)
                    reservationLoader.visibility = View.GONE
                    reservationListsContainer.visibility = View.VISIBLE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                }
            }
        }

    }

    private fun notifyReservations() {
        reservationsAdapter.list = reservationsViewModel.reservations.filter {
            it.toConfirmUsers?.all { it.userId != AppPreferences.user.email } ?: true
        }
        reservationsAdapter.notifyDataSetChanged()
        if (reservationsAdapter.list.isNotEmpty()) {
            reservationsRecyclerView.visibility = View.VISIBLE

        }
    }

    private fun notifyPendingReservations() {
        val pendingReservationsList: List<Reservation> = reservationsViewModel.reservations.filter {
            it.toConfirmUsers?.none { it.userId != AppPreferences.user.email } ?: false
        }
        if (pendingReservationsList.isEmpty()) {
            hidePendingReservations()
            pendingReservations.visibility = View.GONE
            reservationsDivider.visibility = View.GONE
            reservationsArrow.visibility = View.GONE
        } else {
            hidePendingReservations()
            pendingReservationAdapter.list = pendingReservationsList
            pendingReservationAdapter.notifyDataSetChanged()
            reservationsArrow.visibility = View.VISIBLE
        }
    }

    private fun showReservations() {
        reservationsRecyclerView.visibility = View.VISIBLE
        Glide.with(requireContext()).load(R.drawable.ic_arrow_up_24dp).fitCenter()
            .into(reservationsArrow!!)
    }

    private fun hideReservations() {
        reservationsRecyclerView.visibility = View.GONE
        Glide.with(requireContext()).load(R.drawable.ic_arrow_down_24dp).fitCenter()
            .into(reservationsArrow!!)
    }

    private fun showPendingReservations() {
        pendingReservationsRecyclerView.visibility = View.VISIBLE
        Glide.with(requireContext()).load(R.drawable.ic_arrow_up_24dp).fitCenter()
            .into(pendingReservationsArrow!!)
        reservationsDivider.visibility = View.GONE
    }

    private fun hidePendingReservations() {
        pendingReservationsRecyclerView.visibility = View.GONE
        Glide.with(requireContext()).load(R.drawable.ic_arrow_down_24dp).fitCenter()
            .into(pendingReservationsArrow!!)
        reservationsDivider.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
