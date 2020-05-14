package com.example.restopass.main.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R

class SettingsAdapter(val clickListener: SettingListener, private val settings: List<Setting>) :
    RecyclerView.Adapter<SettingsAdapter.MyViewHolder>() {

    private val VIEWTYPE_CATEGORY: Int = 1
    private val VIEWTYPE_BUTTON: Int = 2

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = settings.size

    override fun getItemViewType(position: Int): Int {
        return if(settings[position].isCategory)
            VIEWTYPE_CATEGORY
        else VIEWTYPE_BUTTON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = if (viewType == VIEWTYPE_CATEGORY)
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_category_item, parent, false)
        else LayoutInflater.from(parent.context)
            .inflate(R.layout.view_setting_item, parent, false)

        return MyViewHolder(view)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.settingTitle).text = settings[position].title
        settings[position].image?.let {
            holder.view.findViewById<ImageView>(R.id.settingImage).setImageResource(it)
        }
        holder.view.bind(getItemViewType(position), clickListener)
    }
}

class SettingListener(val clickListener: (settingId: String) -> Unit) {
    fun onClick(setting: Setting) = clickListener(setting.title)
}