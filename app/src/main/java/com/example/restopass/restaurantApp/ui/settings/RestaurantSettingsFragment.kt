package com.example.restopass.restaurantApp.ui.settings

import android.R.id.text1
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.Restaurant
import com.example.restopass.domain.RestaurantViewModel
import com.example.restopass.main.common.restaurant.DishAdapter
import com.example.restopass.utils.AlertDialogUtils
import kotlinx.android.synthetic.main.activity_restaurant.*
import kotlinx.android.synthetic.main.restaurant_fragment_settings.*
import kotlinx.android.synthetic.main.restaurant_fragment_settings.view.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.text.DecimalFormat


class RestaurantSettingsFragment : Fragment() {

    lateinit var dishAdapter : DishAdapter
    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)
    lateinit var restaurantViewModel : RestaurantViewModel
    lateinit var restaurant : Restaurant

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.restaurant_fragment_settings, container, false)
    }

    override fun onStart() {
        super.onStart()

        coroutineScope.launch {
            try {
                restaurantSettingsContainer.visibility = View.GONE
                restaurantSettingsProgressBar.visibility = View.VISIBLE
                restaurantViewModel.get(AppPreferences.restaurantUser!!.restaurant.restaurantId)
                restaurant = restaurantViewModel.restaurant
                setView()
                restaurantSettingsProgressBar.visibility = View.GONE
                restaurantSettingsContainer.visibility = View.VISIBLE
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.statusBarColor = resources.getColor(R.color.backgroundGray)

        restaurantViewModel =
            ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        logout.setOnClickListener {
            AppPreferences.logout()
        }
    }

    private fun setView() {
        dishAdapter = DishAdapter(restaurant.dishes, true, showAvailability = true, parent = this, container = this.restaurantSettingsContainer)

        Glide.with(this).load(restaurant.img).into(restaurantImageSettings)
        view.apply {
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

            restaurantSettingsScore.text = TextUtils.concat(span1, "", span2)
            restaurantSettingsHourToCancel.text = resources.getString(R.string.settingsRestaurantScore, restaurant.hoursToCancel.toString())

            restaurantSettingsDishes.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = dishAdapter
            }
        }
    }

}