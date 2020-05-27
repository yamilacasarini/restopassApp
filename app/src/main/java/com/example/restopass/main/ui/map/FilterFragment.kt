package com.example.restopass.main.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : Fragment() {
    private lateinit var mapViewModel: MapViewModel
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var reciclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        filterAdapter = FilterAdapter(mapViewModel)
        reciclerView =  checkboxRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = filterAdapter
        }
    }

}
