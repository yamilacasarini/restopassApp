package com.example.restopass.restaurantApp.ui.settings

import android.R.id.text1
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.main.common.restaurant.DishAdapter
import kotlinx.android.synthetic.main.restaurant_fragment_settings.*
import kotlinx.android.synthetic.main.restaurant_fragment_settings.view.*
import java.text.DecimalFormat


class RestaurantSettingsFragment : Fragment() {

    lateinit var dishAdapter : DishAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.restaurant_fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val restaurant = AppPreferences.restaurantUser!!.restaurant
        dishAdapter = DishAdapter(restaurant.dishes, true, showAvailability = true)

        view.apply {
            Glide.with(this).load(restaurant.img).into(restaurantImageSettings)
            restaurantSettingsTitle.text = restaurant.name
            restaurantSettingsDirection.text =restaurant.address
            val span1 = SpannableString(DecimalFormat("#.#").format(restaurant.stars))
            span1.setSpan(
                AbsoluteSizeSpan(70),
                0,
                DecimalFormat("#.#").format(restaurant.stars).length,
                SPAN_INCLUSIVE_INCLUSIVE
            )

            val span2 = SpannableString("/5")
            span2.setSpan(
                AbsoluteSizeSpan(40),
                0,
                "/5".length,
                SPAN_INCLUSIVE_INCLUSIVE
            )
            val finalText = TextUtils.concat(span1, "", span2)
            restaurantSettingsScore.text = finalText
            restaurantSettingsHourToCancel.text = resources.getString(R.string.settingsRestaurantScore, restaurant.hoursToCancel.toString())

            restaurantSettingsDishes.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = dishAdapter
            }
        }
        logout.setOnClickListener {
            AppPreferences.logout()
        }
    }

}