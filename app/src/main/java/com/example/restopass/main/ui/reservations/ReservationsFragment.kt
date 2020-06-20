package com.example.restopass.main.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restopass.R
import com.example.restopass.domain.ReservationViewModel
import com.example.restopass.main.common.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reservations.*
import kotlinx.coroutines.*
import timber.log.Timber


class ReservationsFragment : Fragment() {

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var reservationsAdapter: ReservationsAdapter

    private lateinit var reservationsViewModel : ReservationViewModel

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

        reservationsViewModel = ViewModelProvider(requireActivity()).get(ReservationViewModel::class.java)

        rootView = inflater.inflate(R.layout.fragment_reservations, container, false)
        return rootView;
    }

    override fun onStart() {
        super.onStart()

        reservationLoader.visibility = View.VISIBLE
        coroutineScope.launch {
            try {
                reservationsViewModel.get()
                reservationsAdapter.list = reservationsViewModel.reservations
                reservationsAdapter.notifyDataSetChanged()

                reservationLoader.visibility = View.GONE
                reservationsRecyclerView.visibility = View.VISIBLE

                arguments?.get("reservationId")?.apply {
                    val reservationIndex = reservationsViewModel.reservations
                        .indexOfFirst { it.reservationId == this }

                    if (reservationIndex > 0)
                    reservationsRecyclerView.doOnLayout {
                        reservationsRecyclerView.smoothScrollToPosition(reservationIndex)
                    }
                }

            } catch (e: Exception) {
                if(isActive) {
                    Timber.e(e)
                    reservationLoader.visibility = View.GONE
                    val titleView: View =
                        layoutInflater.inflate(R.layout.alert_dialog_title, container, false)
                    AlertDialog.getAlertDialog(
                        context,
                        titleView,
                        view
                    ).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        reservationsAdapter = ReservationsAdapter(this);

        val linearLayoutManager = LinearLayoutManager(activity)
        reservationsRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = reservationsAdapter
        }
    }

    fun cancelReservation(reservationId: String) {
        reservationLoader.visibility = View.VISIBLE
        coroutineScope.launch {
            try {
                reservationsViewModel.cancel(reservationId)
                reservationsAdapter.list = reservationsViewModel.reservations
                reservationsAdapter.notifyDataSetChanged()

                reservationLoader.visibility = View.GONE
                reservationsRecyclerView.visibility = View.VISIBLE
            } catch (e: Exception) {
                if(isActive) {
                    Timber.e(e)
                    reservationLoader.visibility = View.GONE
                    val titleView: View =
                        layoutInflater.inflate(R.layout.alert_dialog_title, container, false)
                    AlertDialog.getAlertDialog(
                        context,
                        titleView,
                        view
                    ).show()
                }
            }
        }

    }
}
