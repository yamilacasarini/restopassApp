package com.example.restopass.main.ui.settings

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import com.example.restopass.R
import kotlinx.android.synthetic.main.view_setting_item.view.*

class SettingItemView
@JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyle: Int = 0) : LinearLayout(context, attrs, defStyle) {
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_setting_item, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SettingItemView,
            0,0).apply {
            try {
                settingImage.setImageResource(getResourceId(R.styleable.SettingItemView_settingImage, 0))
                settingText.text = getString(R.styleable.SettingItemView_settingText)
            } finally {
                recycle()
            }
        }
    }
}