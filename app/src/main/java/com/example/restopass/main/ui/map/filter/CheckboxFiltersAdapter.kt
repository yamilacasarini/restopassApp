package com.example.restopass.main.ui.map.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.main.ui.map.MapViewModel
import kotlinx.android.synthetic.main.view_filter_checkbox_item.view.*

class CheckboxFilterAdapter(
    private val modelViewModel: MapViewModel,
    private val filterFragment: FilterFragment
) :
    RecyclerView.Adapter<CheckboxFilterAdapter.FilterViewHolder>() {

    private val tags = modelViewModel.filters!!.tags

    override fun getItemCount() = tags.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder =
        FilterViewHolder.from(
            parent
        )

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.itemView.apply {
            filterCheckbox.text = tags[position]
            filterCheckbox.isChecked = modelViewModel.selectedFilters.tags.contains(tags[position])
            filterCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                //TODO: Fijarme si es checked o no y sacar o poner en selectedFilters
                if (isChecked) {
                    filterFragment.selectedFilters.tags.add(tags[position])
                } else {
                    filterFragment.selectedFilters.tags.remove(tags[position])
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
