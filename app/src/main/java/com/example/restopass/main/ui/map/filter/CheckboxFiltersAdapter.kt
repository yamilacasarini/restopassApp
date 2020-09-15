package com.example.restopass.main.ui.map.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.main.ui.map.MapViewModel
import kotlinx.android.synthetic.main.view_filter_checkbox_item.view.*

class CheckboxFilterAdapter(
    private val mapViewModel: MapViewModel,
    private val filterFragment: FilterFragment,
    private val tags: List<String>
) :
    RecyclerView.Adapter<CheckboxFilterAdapter.FilterViewHolder>() {

    override fun getItemCount() = tags.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder =
        FilterViewHolder.from(
            parent
        )

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.itemView.apply {
            filterCheckbox.text = tags[position]
            filterCheckbox.isChecked = mapViewModel.selectedFilters.tags.contains(tags[position])
            filterCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                //TODO: Fijarme si es checked o no y sacar o poner en selectedFilters
                if (isChecked) {
                    mapViewModel.selectedFilters.tags.add(tags[position])
                } else {
                    mapViewModel.selectedFilters.tags.remove(tags[position])
                }
            }
        }
    }

    class FilterViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): FilterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.view_filter_checkbox_item, parent, false)
                return FilterViewHolder(
                    view
                )
            }
        }
    }
}
