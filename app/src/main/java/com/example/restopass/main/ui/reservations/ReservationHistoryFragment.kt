package com.example.restopass.main.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restopass.R
import com.example.restopass.domain.ReservationViewModel
import com.example.restopass.main.MainActivity
import com.example.restopass.utils.AlertDialogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reservations.*
import kotlinx.android.synthetic.main.fragment_reservations_history.*
import kotlinx.coroutines.*
import timber.log.Timber


class ReservationHistoryFragment : Fragment() {

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var reservationsAdapter: ReservationsAdapter

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

        rootView = inflater.inflate(R.layout.fragment_reservations_history, container, false)
        return rootView;
    }

    override fun onStart() {
        super.onStart()

        historyReservationLoader.visibility = View.VISIBLE
        coroutineScope.launch {
            try {
                reservationsViewModel.getHistory()

                if (reservationsViewModel.reservations.isEmpty()) {
                    findNavController().navigate(R.id.emptyReservationFragment)
                }

                reservationsAdapter.list = reservationsViewModel.reservations
                reservationsAdapter.notifyDataSetChanged()

                historyReservationLoader.visibility = View.GONE

            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    reservationLoader.visibility = View.GONE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        reservationsAdapter = ReservationsAdapter(ReservationsFragment())
        val linearLayoutManager = LinearLayoutManager(activity)
        historyReservationsRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = reservationsAdapter
        }

        (activity as MainActivity).topAppBar?.apply {
            setTitle(R.string.history_reservations_title)
            visibility = View.VISIBLE
        }

    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}
