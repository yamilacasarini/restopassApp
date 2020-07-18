package com.example.restopass.main.common.membership

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.common.orElse
import com.example.restopass.connection.Api4xxException
import com.example.restopass.domain.Membership
import com.example.restopass.domain.MembershipsViewModel
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.main.common.AlertBody
import com.example.restopass.main.ui.settings.payment.PaymentViewModel
import com.example.restopass.utils.AlertDialogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_membership.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import timber.log.Timber


class MembershipFragment : Fragment(), MembershipAdapterListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var membershipAdapter: MembershipAdapter
    private lateinit var membershipsViewModel: MembershipsViewModel

    private lateinit var paymentViewModel: PaymentViewModel

    val job = Job()
    val coroutineScope = CoroutineScope(job + Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_membership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membershipsViewModel =
            ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)

        paymentViewModel = ViewModelProvider(requireActivity()).get(PaymentViewModel::class.java)

        membershipAdapter = MembershipAdapter(this)
        recyclerView = membershipRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = membershipAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        notEnrolledLoader.visibility = View.VISIBLE
        coroutineScope.launch {
            val deferred = mutableListOf(getMemberships())
            if (paymentViewModel.creditCard == null) {
                deferred.add(getUserCreditCard())
            }

            deferred.awaitAll()

            (activity as AppCompatActivity).supportActionBar?.apply {
                setTitle(R.string.membershipToolbarTitle)
                show()
            }

            notEnrolledLoader.visibility = View.GONE
        }
    }

    private fun getMemberships(): Deferred<Unit> {
        return coroutineScope.async {
            try {
                membershipsViewModel.get()

                membershipAdapter.memberships = formatMembershipList(membershipsViewModel)
                membershipAdapter.notifyDataSetChanged()
                membershipRecycler.visibility = View.VISIBLE

            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    notEnrolledLoader.visibility = View.GONE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container, view).show()
                }
            }
        }
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
                    notEnrolledLoader.visibility = View.GONE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container, view).show()
                }
            }
        }
    }

    override fun onEnrollClick(membership: Membership) {
        paymentViewModel.creditCard?.let {
            val title = getString(R.string.chargeCreditCardTitle, membership.name)
            val description = getString(R.string.chargeCreditCardDescription, it.type.replace("_", " "), it.lastFourDigits)
            AlertDialog.getActionDialogWithParams(
                context,
                layoutInflater, membershipContainer, ::updateMembership,
                membership, AlertBody(title, description, R.string.aceptChargeCreditCard)
            ).show()
        }.orElse {
            membershipsViewModel.selectedUpdateMembership = membership
            findNavController().navigate(R.id.paymentFragment)
        }
    }

    private fun updateMembership(membership: Any) {
        membershipRecycler.visibility = View.GONE
        notEnrolledLoader.visibility = View.VISIBLE
        coroutineScope.launch {
            try {
                membershipsViewModel.update(membership as Membership)
                findNavController().navigate(R.id.navigation_enrolled_home)
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    notEnrolledLoader.visibility = View.GONE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container, view).show()
                }
            }
        }
    }


    override fun onDetailsClick(membership: Membership) {
        membershipsViewModel.selectedDetailsMembership = membership
    }

    private fun formatMembershipList(response: MembershipsViewModel): List<Membership> {
        val actualMembershipTitle =
            Membership(
                name = "Tu Membresía",
                isTitle = true
            )
        val otherMembershipsTitle =
            Membership(
                name = "Otras Membresías",
                isTitle = true
            )
        val membershipList = response.memberships.toMutableList()
        membershipList.apply {
            response.actualMembership?.let {
                it.isActual = true
                add(0, actualMembershipTitle)
                add(1, it)
                add(2, otherMembershipsTitle)
            }
        }
        return membershipList
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}