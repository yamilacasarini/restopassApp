package com.example.restopass.main.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import java.util.*


class ReservationsFragment : Fragment() {

    data class ReservationData(
        val title: String,
        val address: String,
        val date: String,
        val status: String
    ) : Comparable<ReservationData> {
        override fun compareTo(other: ReservationData): Int {
            return if (this.status > other.status) 1 else -1
        }

    }

    private lateinit var rootView: View;

    private val mNicolasCageMovies = listOf(
        ReservationData(
            "La Nueva Casa Japonesa",
            "Humberto 1ero 2300, CABA",
            "Jueves 20 de Mayo, 22hs 1 pers",
            "CONFIRMED"
        ),
        ReservationData(
            "La Causa Nikkei",
            "Av Callao 1200, CABA",
            "Jueves 20 de Mayo, 22hs 2 pers",
            "CANCELED"
        ),
        ReservationData(
            "Saigon",
            "Marcelo T Alvear 1200, CABA",
            "Jueves 20 de Mayo, 22hs 2 pers",
            "CONFIRMED"
        ),
        ReservationData(
            "El Cuartito",
            "Talcahuano 1200, CABA",
            "Jueves 20 de Mayo, 22hs 2 pers",
            "DONE"
        ),
        ReservationData(
            "Guerrin",
            "Av Corrientes 1200, CABA",
            "Jueves 20 de Mayo, 22hs 2 pers",
            "DONE"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_reservations, container, false)
        return rootView;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView.findViewById<RecyclerView>(R.id.settingsRecyclerView).apply {
            layoutManager = LinearLayoutManager(activity)
            Collections.sort(mNicolasCageMovies)
            adapter = ReservationsAdapter(mNicolasCageMovies)
        }
    }

}
