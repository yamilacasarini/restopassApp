package com.example.restopass.main.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.databinding.ViewMembershipItemBinding
import kotlinx.android.synthetic.main.view_filter_checkbox_item.view.*

class FilterAdapter(private val modelViewModel: MapViewModel) :
    RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    val tags = modelViewModel.filters!!.tags

    override fun getItemCount() = tags.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder = FilterViewHolder.from(parent)

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.itemView.apply {
            filterCheckbox.text = tags[position]
            filterCheckbox.setOnClickListener {
                //TODO: Fijarme si es checked o no y sacar o poner en selectedFilters
                if (filterCheckbox.isChecked) {
                    modelViewModel.selectedFilters.add(tags[position])
                } else {
                    modelViewModel.selectedFilters.remove(tags[position])
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
                return FilterViewHolder(view)
            }
        }
    }
}
