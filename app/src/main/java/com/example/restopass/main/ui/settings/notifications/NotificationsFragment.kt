package com.example.restopass.main.ui.settings.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.main.MainActivity
import com.example.restopass.main.common.AlertBody
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.service.UserService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.coroutines.*
import timber.log.Timber

class NotificationsFragment : Fragment() {

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).topAppBar?.apply {
            setTitle(R.string.notifications)
            visibility = View.VISIBLE
        }

        switchNotifications.apply {
            this.isChecked = AppPreferences.user.isSubscribedToTopic

            setOnCheckedChangeListener { _, isChecked ->
                AppPreferences.user.isSubscribedToTopic = isChecked

                if (isChecked) subscribe()
                else onUnsubscribeSwitch()
            }
        }
    }

    private fun subscribe() {
        AppPreferences.user.let {
            AppPreferences.user = it.copy(isSubscribedToTopic = true)
        }

        coroutineScope.launch {
            try {
                UserService.subscribeToTopic()
                FirebaseMessaging.getInstance().subscribeToTopic(AppPreferences.user.firebaseTopic)
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    AppPreferences.user.let {
                        AppPreferences.user = it.copy(isSubscribedToTopic = false)
                    }
                }
            }
        }
    }

    private fun unsubscribe() {
        AppPreferences.user.let {
            AppPreferences.user = it.copy(isSubscribedToTopic = false)
        }

        coroutineScope.launch {
            try {
                UserService.unsubscribeToTopic()
                FirebaseMessaging.getInstance().unsubscribeFromTopic(AppPreferences.user.firebaseTopic)
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    AppPreferences.user.let {
                        AppPreferences.user = it.copy(isSubscribedToTopic = true)
                    }
                }
            }
        }
    }

    private fun onUnsubscribeSwitch() {
        AlertDialog.getActionDialog(
            context,
            layoutInflater, notificationsContainer, ::unsubscribe,
            AlertBody(
                getString(R.string.notificationsAlertDialogTitle),
            positiveActionText = R.string.accept, negativeActionText = R.string.cancelAlertMessage)
        ) { switchNotifications.isChecked = true }.show()
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

}