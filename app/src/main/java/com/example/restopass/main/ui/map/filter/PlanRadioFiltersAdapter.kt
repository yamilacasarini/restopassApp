package com.example.restopass.main.ui.map.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.main.ui.map.MapViewModel
import kotlinx.android.synthetic.main.view_filter_radio_item.view.*


class PlanRadioFiltersAdapter(private val mapViewModel: MapViewModel, private val filterFragment: FilterFragment) :
    RecyclerView.Adapter<PlanRadioFiltersAdapter.FilterViewHolder>() {

    private var checkedRadio: CompoundButton? = null

    private val plans = mapViewModel.filters.plans

    override fun getItemCount() = plans.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder =
        FilterViewHolder.from(
            parent
        )

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.itemView.apply {
            filterRadio.text = plans[position].name
            filterRadio.isChecked = plans[position].membershipId == mapViewModel.selectedFilters.plan
            if (filterRadio.isChecked) {
                checkedRadio = filterRadio
            }
            filterRadio.setOnCheckedChangeListener { radio: CompoundButton, isChecked: Boolean ->
                checkedRadio?.let { it.isChecked = false }
                checkedRadio = radio
                if (isChecked) mapViewModel.selectedFilters.plan = plans[position].membershipId
            }
        }
    }

    class FilterViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): FilterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.view_filter_radio_item, parent, false)
                return FilterViewHolder(
                    view
                )
            }
        }
    }

}
