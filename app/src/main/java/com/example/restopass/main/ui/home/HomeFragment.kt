package com.example.restopass.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.main.ui.PlanActivity

class HomeFragment : Fragment(), HomeListener{

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var rootView : View;

    private val plans = listOf(
        PlanData("Plan 1", 10.0),
        PlanData("Plan 2", 10.0),
        PlanData("Plan 3", 10.0),
        PlanData("Plan 4", 10.0),
        PlanData("Plan 5", 10.0)
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

        val homeAdapter = HomeAdapter(plans)

        homeAdapter.listener = this
        rootView.findViewById<RecyclerView>(R.id.home_recycler_view).apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = homeAdapter
        }
    }

    override fun onClick(plan: PlanData?) {
        val intent: Intent = Intent(requireActivity(), PlanActivity::class.java)
        intent.putExtra("plan", plan)
        requireActivity().startActivity(intent)
    }
}
