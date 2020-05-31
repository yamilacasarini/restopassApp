package com.example.restopass.main.ui.settings

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.databinding.ViewSettingItemBinding
import com.example.restopass.login.LoginActivity
import com.example.restopass.main.MainActivity
import kotlinx.android.synthetic.main.view_item_button.view.*
import kotlinx.android.synthetic.main.view_setting_item.view.*
import timber.log.Timber
import java.lang.ClassCastException

class SettingsAdapter(private val settings: List<Setting>, val listener: SettingListener) :
    ListAdapter<Setting, RecyclerView.ViewHolder>(SettingDiffCallback()) {

    private val VIEWTYPE_CATEGORY = 1
    private val VIEWTYPE_CATEGORY_BUTTON = 2
    private val VIEWTYPE_BUTTON = 3

    //EL ORDEN DE LLAMADAS ES ESTE
    override fun getItemCount() = settings.size

    override fun getItemViewType(position: Int): Int {
        return if(settings[position].typeButton in ButtonSettingType.values()){
            return if (settings[position].typeButton == ButtonSettingType.SESSION) VIEWTYPE_BUTTON
            else  VIEWTYPE_CATEGORY_BUTTON
        }
        else VIEWTYPE_CATEGORY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEWTYPE_CATEGORY -> CategoryViewHolder.from(parent)
            VIEWTYPE_CATEGORY_BUTTON -> CategoryButtonViewHolder.from(parent)
            VIEWTYPE_BUTTON -> ButtonViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            settingTitle?.setText(settings[position].title)

            settings[position].image?.let {
               settingImage.setImageResource(it)
            }

            logoutButton?.apply {
                setText(settings[position].title)
                setOnClickListener {
                    AppPreferences.removeAllPreferences()
                    val intent = Intent(this.context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }

        if (holder is CategoryButtonViewHolder) {
            holder.bind(listener, settings[position])
        }
    }

    class CategoryButtonViewHolder private constructor(val binding: ViewSettingItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: SettingListener, item: Setting) {
            binding.setting = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CategoryButtonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ViewSettingItemBinding.inflate(layoutInflater, parent, false)

                return CategoryButtonViewHolder(binding)
            }
        }
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.view_category_item, parent, false)
                return CategoryViewHolder(view)
            }
        }
    }

    class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): ButtonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.view_item_button, parent, false)
                return ButtonViewHolder(view)
            }
        }
    }

}

class SettingDiffCallback : DiffUtil.ItemCallback<Setting>() {
    override fun areItemsTheSame(oldItem: Setting, newItem: Setting) = oldItem.typeButton == newItem.typeButton
    override fun areContentsTheSame(oldItem: Setting, newItem: Setting): Boolean = true
}

class SettingListener(val clickListener: (buttonSettingType: ButtonSettingType?) -> Unit) {
    fun onClick(setting: Setting) = clickListener(setting.typeButton)
}

