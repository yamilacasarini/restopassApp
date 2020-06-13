package com.example.restopass.main.ui.map.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.main.ui.map.MapFragment
import com.example.restopass.main.ui.map.MapViewModel
import kotlinx.android.synthetic.main.view_filter_checkbox.*
import kotlinx.android.synthetic.main.view_filter_checkbox.view.*
import kotlinx.android.synthetic.main.view_filter_checkbox_item.view.*

class RestoPreviewAdapter(
    private val modelViewModel: MapViewModel,
    private val mapFragment: MapFragment
) :
    RecyclerView.Adapter<RestoPreviewAdapter.RestoPreviewHolder>() {

    override fun getItemCount() = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoPreviewHolder =
        RestoPreviewHolder.from(
            parent
        )

    override fun onBindViewHolder(holder: RestoPreviewHolder, position: Int) {
        holder.itemView.apply {

        }
    }

    class RestoPreviewHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): RestoPreviewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.restaurant_preview_item, parent, false)
                return RestoPreviewHolder(
                    view
                )
            }
        }
    }
}
