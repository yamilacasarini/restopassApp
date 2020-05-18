package com.example.restopass.main.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import java.math.BigDecimal

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var rootView : View;

    data class PlanData(val title: String, val price: BigDecimal)

    private val plans = listOf(
        PlanData("Plan 1", BigDecimal.TEN),
        PlanData("Plan 2", BigDecimal.TEN),
        PlanData("Plan 3", BigDecimal.TEN),
        PlanData("Plan 4", BigDecimal.TEN),
        PlanData("Plan 5", BigDecimal.TEN)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeAdapter = HomeAdapter(plans, HomeListener { plan ->
            Toast.makeText(context, "Se seleccionó: ${plan.title}", Toast.LENGTH_LONG).show()
        })

        rootView.findViewById<RecyclerView>(R.id.home_recycler_view).apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = homeAdapter
        }
    }
}
