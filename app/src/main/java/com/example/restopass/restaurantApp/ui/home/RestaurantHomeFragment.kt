package com.example.restopass.restaurantApp.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.DoneReservationViewModel
import com.example.restopass.utils.AlertDialogUtils
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_restaurant.*
import kotlinx.android.synthetic.main.restaurant_fragment_home.view.*
import kotlinx.coroutines.*
import timber.log.Timber


class RestaurantHomeFragment : Fragment() {

    var qrScan: IntentIntegrator? = null
    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var doneReservationViewModel: DoneReservationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.restaurant_fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qrScan = IntentIntegrator(activity)
        qrScan?.captureActivity = QRActivity::class.java

        doneReservationViewModel =
            ViewModelProvider(requireActivity()).get(DoneReservationViewModel::class.java)

        activity?.window?.statusBarColor = resources.getColor(R.color.backgroundGray)

        view.apply {
            qrScanButton.setOnClickListener {
                qrScan?.initiateScan()
            }
            qrManualButton.setOnClickListener {
                setVisibilityMainBtns(View.GONE)
                setVisibilitySubmitBtns(View.VISIBLE)
            }
            dishesSubmitCancelBtn.setOnClickListener {
                setVisibilitySubmitBtns(View.GONE)
                setVisibilityMainBtns(View.VISIBLE)
            }
            dishesSubmitAcceptBtn.setOnClickListener {
                restaurantHomeLayout.visibility = View.GONE
                restaurantHomeProgressBar.visibility = View.VISIBLE
                coroutineScope.launch {
                    try {
                        doneReservationViewModel.done(
                            reservationNumberInput.text.toString(),
                            emailInput.text.toString(),
                            AppPreferences.restaurantUser!!.restaurant.restaurantId
                        )

                        restaurantHomeProgressBar.visibility = View.GONE
                        findNavController().navigate(R.id.restaurantDishesFragment)

                    } catch (e: Exception) {
                        if (isActive) {
                            Timber.e(e)
                            AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                        }
                    }
                }
            }
        }
    }

    private fun setVisibilitySubmitBtns(visibility: Int) {
        view?.apply {
            ownerEmailInput.visibility = visibility
            reservationInput.visibility = visibility
            dishesSubmitManualChargeButtonLayout.visibility = visibility
        }
    }

    private fun setVisibilityMainBtns(visibility: Int) {
        view?.apply {
            qrScanButton.visibility = visibility
            qrManualButton.visibility = visibility
        }
    }


}

