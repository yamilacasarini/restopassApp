package com.example.restopass.main.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R


class ReservationsFragment : Fragment() {

    data class ReservationData(val title: String, val address: String, val date: String, val status: String)
    private lateinit var rootView : View;

    private val mNicolasCageMovies = listOf(
        ReservationData("Raising Arizona", "Calle Falsa","1987", "CONFIRMED"),
        ReservationData("Vampire's Kiss", "Calle Falsa","1988", "DONE"),
        ReservationData("Con Air", "Calle Falsa","1997", "DONE"),
        ReservationData("Gone in 60 Seconds", "Calle Falsa","1997", "DONE"),
        ReservationData("National Treasure", "Calle Falsa","2004", "DONE"),
        ReservationData("The Wicker Man", "Calle Falsa","2006", "DONE"),
        ReservationData("Ghost Rider", "Calle Falsa","2007", "DONE"),
        ReservationData("Knowing", "Calle Falsa","2009", "DONE")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false)
        return rootView;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView.findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ReservationsAdapter(mNicolasCageMovies)
        }
    }

}
