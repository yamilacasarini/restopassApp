package com.example.restopass.main.common.restaurant

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.common.orElse
import com.example.restopass.connection.Api4xxException
import com.example.restopass.domain.*
import com.example.restopass.main.MainActivity
import com.example.restopass.main.common.AlertCreditCard
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.main.ui.settings.payment.PaymentViewModel
import com.example.restopass.utils.AlertDialogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.android.synthetic.main.fragment_restaurants_list.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.time.LocalDateTime

class RestaurantFragment : Fragment() {
    private lateinit var tagRecyclerView: RecyclerView
    private lateinit var tagAdapter: TagAdapter

    private lateinit var dishRecyclerView: RecyclerView
    private lateinit var dishAdapter: DishAdapter

    private lateinit var membershipsViewModel: MembershipsViewModel
    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var paymentViewModel: PaymentViewModel


    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membershipsViewModel =
            ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)
        restaurantViewModel =
            ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)
        paymentViewModel = ViewModelProvider(requireActivity()).get(PaymentViewModel::class.java)


        val restaurant = restaurantViewModel.restaurant


        tagAdapter = TagAdapter()

        tagAdapter.tags = restaurant.tags
        tagAdapter.notifyDataSetChanged()

        tagRecyclerView = tagRestaurantRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = tagAdapter
        }

        val isMembershipSelected = arguments?.getBoolean("isMembershipSelected")

        // Si viene de una Membership Card, mostramos los platos en el órden de esa membresía.
        // Si viene de otro lado y está enrolado, mostramos los platos en el órden de inclusión de su membresía.
        // En cualquier otro caso, mostramos los platos por como vienen
        val sortedDishes = if (isMembershipSelected == true) {
            restaurant.dishes.sortedBy {
                !it.isIncluded(membershipsViewModel.selectedDetailsMembership!!.membershipId!!)
            }
        } else {
            AppPreferences.user.actualMembership?.run {
                restaurant.dishes.sortedBy {
                    !it.isIncluded(this)
                }
            }
        }

        dishAdapter = DishAdapter(sortedDishes ?: restaurant.dishes, parent = this,  container = this.restaurantContainer)
        dishAdapter.notifyDataSetChanged()
        if (isMembershipSelected == true) {
            dishAdapter.selectedMembership = membershipsViewModel.selectedDetailsMembership
        }

        dishRecyclerView = dishRecyclerV.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = dishAdapter
        }

        val selectedMembership =
            isMembershipSelected?.run { membershipsViewModel.selectedDetailsMembership }
        fillView(restaurant, selectedMembership)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillView(restaurant: Restaurant, selectedMembership: Membership?) {
        AppPreferences.user.favoriteRestaurants?.let {
            if (it.contains(restaurant.restaurantId)) {
                changeFavoriteIcon(R.drawable.ic_favorite_full)
            } else {
                changeFavoriteIcon(R.drawable.ic_favorite_empty)
            }
        }

        favoriteButton.setOnClickListener {
            toggleFavorite(restaurant)
        }


        restaurant.let {
            Glide.with(this).load(it.img).into(restaurantImage)
            restaurantName.text = it.name
            restaurantAddress.text = it.address
            restaurantRating.rating = it.stars

            it.comments?.apply {
                showCommentsButton.visibility = View.VISIBLE
                showCommentsButton.text = resources.getString(R.string.showCommentsButton, it.comments.size.toString())

                showCommentsButton.setOnClickListener {
                    findNavController().navigate(R.id.commentFragment)
                }
            }

        }


        //Si tiene membresía, viene de una tarjeta Membresía y es la suya => se le muestra "Reservar Mesa"
        // Si tiene membresía, NO viene de una tarjeta Membresía y el restaurant está en su membresía => se le muestra "Reservar Mesa"
        // Si no tiene membresía pero aun esta dentro del mes que cancelo.
        // Decidimos no mostrar "Reservar Mesa" si viene de una tarjeta Membresía, "detalles" porque ese resto puede tener
        // varios platos que no están en su membresía y presta a confusión
        AppPreferences.user.actualMembership?.let {
            if (isActualMembership(it, selectedMembership) ||
                (selectedMembership == null && isRestaurantInMembership(it, restaurant))
            ) {
                setBookTableButton()
            } else setButtonByMembership(selectedMembership)
        }.orElse {
            if(hasFinalizeDateAndIsValid()) {
                setBookTableButton()
            } else {
                setButtonByMembership(selectedMembership)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            restaurantScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY) {
                    restaurantFloatingButton.hide()
                } else {
                    restaurantFloatingButton.show()
                }
            }
        }

        (activity as MainActivity).mainBackButton.visibility = View.VISIBLE

    }

    private fun setBookTableButton() {
        restaurantFloatingButton.setOnClickListener {
            if (AppPreferences.user.visits <= 0) {
                showNoMoreVisitsDialog()
            } else {
                it.findNavController().navigate(R.id.reservationCreateStep1)
            }
        }
        restaurantFloatingButton.setText(R.string.bookTable)
    }

    private fun toggleFavorite(restaurant: Restaurant) {
        AppPreferences.user.favoriteRestaurants?.let {
            if (it.contains(restaurant.restaurantId)) {
                unfavorite(restaurant)
            } else {
                favorite(restaurant)
            }
        }.orElse {
            favorite(restaurant)
        }
    }

    private fun unfavorite(restaurant: Restaurant) {
        removeRestaurantToFavorites(restaurant)
        coroutineScope.launch {
            try {
                (activity as MainActivity).unfavorite(restaurant)
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    addRestaurantToFavorites(restaurant)
                }
            }
        }
    }

    private fun favorite(restaurant: Restaurant) {
        addRestaurantToFavorites(restaurant)
        coroutineScope.launch {
            try {
                (activity as MainActivity).favorite(restaurant)
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    removeRestaurantToFavorites(restaurant)
                }
            }
        }
    }

    private fun addRestaurantToFavorites(restaurant: Restaurant) {
        changeFavoriteIcon(R.drawable.ic_favorite_full)
        AppPreferences.user.apply {
            var restaurants = this.favoriteRestaurants
            restaurants?.add(restaurant.restaurantId)
                .orElse { restaurants = mutableListOf(restaurant.restaurantId) }
            AppPreferences.user = this.copy(favoriteRestaurants = restaurants)
        }
    }

    private fun removeRestaurantToFavorites(restaurant: Restaurant) {
        changeFavoriteIcon(R.drawable.ic_favorite_empty)
        AppPreferences.user.apply {
            val restaurants = this.favoriteRestaurants
            restaurants?.remove(restaurant.restaurantId)
            AppPreferences.user = this.copy(favoriteRestaurants = restaurants)
        }
    }

    private fun changeFavoriteIcon(drawable: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            favoriteButton.setImageDrawable(requireContext().getDrawable(drawable))
            favoriteButton.setColorFilter(requireContext().getColor(R.color.restoPassGreen))
        } else {
            Glide.with(this).load(drawable).into(favoriteButton)
        }
    }

    private fun isRestaurantInMembership(membershipId: Int, restaurant: Restaurant): Boolean {
        return restaurant.dishes.any {
            it.isIncluded(membershipId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun hasFinalizeDateAndIsValid(): Boolean {
        if(AppPreferences.user.membershipFinalizeDate != null) {
            return LocalDateTime.parse(AppPreferences.user.membershipFinalizeDate)
                .isAfter(LocalDateTime.now())
        }

        return false
    }

    private fun isActualMembership(
        actualMembershipId: Int,
        selectedMembership: Membership?
    ): Boolean {
        return actualMembershipId == selectedMembership?.membershipId
    }

    private fun toggleLoader() {
        restaurantLoader.visibility =
            if (restaurantLoader.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        val visibility =
            if (restaurantLoader.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        restaurantScrollView.visibility = visibility
        restaurantFloatingButton.visibility = visibility
    }

    // Si viene de una tarjeta membresía => le mostramos un "Elegir Membresía X"
    //Sino => Ver membresías
    private fun setButtonByMembership(membership: Membership?) {
        membership?.let {
            val chooseMembership = resources.getString(R.string.chooseMembership, membership.name)
            restaurantFloatingButton.text = chooseMembership
            restaurantFloatingButton.setOnClickListener {
                onEnrollClick(membership)
            }
        }.orElse {
            restaurantFloatingButton.apply {
                setText(R.string.showMemberships)
                setOnClickListener {
                    findNavController().navigate(R.id.membershipsFragment)
                }
            }
        }
    }

    private fun onEnrollClick(membership: Membership) {
        paymentViewModel.creditCard?.let {
            AlertDialog.getActionDialog(
                context,
                layoutInflater, restaurantContainer, ::updateMembership,
                AlertCreditCard(resources, it, membership.name)
            ).show()
        }.orElse {
            membershipsViewModel.selectedUpdateMembership = membership
            findNavController().navigate(R.id.paymentFragment)
        }
    }

    private fun updateMembership() {
        toggleLoader()
        coroutineScope.launch {
            try {
                membershipsViewModel.update(membershipsViewModel.selectedDetailsMembership!!)
                findNavController().navigate(R.id.navigation_enrolled_home)
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    toggleLoader()
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container, view).show()
                }
            }
        }
    }

    private fun showNoMoreVisitsDialog() {
        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        requireView().findNavController().navigate(R.id.membershipsFragment)
                    }
                }
            }
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireView().context)
        builder.setMessage(R.string.no_more_visits)
            .setPositiveButton("Ver Membresias", dialogClickListener)
            .show()
    }

    private fun getUserCreditCard(): Deferred<Unit> {
        return coroutineScope.async {
            try {
                paymentViewModel.get()
            } catch (e: Api4xxException) {
                if (e.error?.code != 40405) throw e
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    restaurantsListLoader.visibility = View.GONE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container, view).show()
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()

        if (paymentViewModel.creditCard == null) {
            toggleLoader()
            coroutineScope.launch {
                getUserCreditCard().await()
                toggleLoader()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}