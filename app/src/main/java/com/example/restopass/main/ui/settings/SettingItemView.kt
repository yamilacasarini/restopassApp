package com.example.restopass.main.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.restopass.R
import kotlinx.android.synthetic.main.view_setting_item.view.*

class SettingItemView
@JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyle: Int = 0) : RelativeLayout(context, attrs, defStyle), View.OnClickListener {
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_setting_item, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SettingItemView,
            0,0).apply {
            try {
                settingImage.setImageResource(getResourceId(R.styleable.SettingItemView_settingImage, 0))
                settingTitle.text = getString(R.styleable.SettingItemView_settingText)
            } finally {
                recycle()
            }
        }
        settingItem.setOnClickListener(this)
        settingItem.isClickable = true
    }


    override fun onClick(v: View?) {
        Toast.makeText(context, "Pasó", Toast.LENGTH_LONG).show()
    }
}